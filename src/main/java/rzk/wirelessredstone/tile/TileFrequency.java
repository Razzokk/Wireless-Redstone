package rzk.wirelessredstone.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.rsnetwork.Device;

import javax.annotation.Nullable;

public class TileFrequency extends TileEntity implements Device.Block
{
	private Device.Type type;
	private short frequency;

	public TileFrequency() {}

	public TileFrequency(Device.Type type)
	{
		this.type = type;
		this.frequency = 0;
	}

	@Override
	public short getFrequency()
	{
		return frequency;
	}

	@Override
	public Type getType()
	{
		return type;
	}

	public void setFrequency(short frequency)
	{
		if (this.frequency != frequency)
		{
			RedstoneNetwork network = RedstoneNetwork.get(world);
			IBlockState state = world.getBlockState(pos);

			if (!(isSender() && !state.getValue(BlockFrequency.POWERED)))
				network.changeDeviceFrequency(this, frequency);

			this.frequency = frequency;
			world.notifyBlockUpdate(pos, state, state, 3);
			markDirty();
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(pos, 3, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		frequency = nbt.getShort("frequency");
		type = nbt.getBoolean("type") ? Device.Type.TRANSMITTER : Device.Type.RECEIVER;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setShort("frequency", frequency);
		nbt.setBoolean("type", isSender());
		return nbt;
	}

	@Override
	public void onLoad()
	{
		if (!world.isRemote && isReceiver())
		{
			WorldServer worldServer = (WorldServer) world;
			worldServer.addScheduledTask(() ->
			{
				RedstoneNetwork network = RedstoneNetwork.get(world);
				IBlockState state = world.getBlockState(pos);
				((BlockFrequency) getBlockType()).setPoweredState(state, world, pos, network.isChannelActive(frequency));
			});
		}
	}

	@Override
	public void invalidate()
	{
		if (!world.isRemote)
		{
			RedstoneNetwork network = RedstoneNetwork.get(world, false);

			if (network != null)
				network.removeDevice(this);
		}
	}
}
