package rzk.wirelessredstone.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import rzk.lib.mc.tile.TileType;
import rzk.lib.mc.util.TaskScheduler;
import rzk.wirelessredstone.RedstoneNetwork;

import javax.annotation.Nullable;

public class TileFrequency extends TileEntity
{
	public static final TileEntityType<TileFrequency> TYPE = new TileType<>(TileFrequency::new);

	private int frequency;
	private boolean isTransmitter;

	public TileFrequency(boolean isTransmitter)
	{
		super(TYPE);
		frequency = 0;
		this.isTransmitter = isTransmitter;
	}

	public TileFrequency()
	{
		super(TYPE);
		frequency = 0;
		isTransmitter = false;
	}

	public int getFrequency()
	{
		return frequency;
	}

	public void setFrequency(int frequency)
	{
		if (frequency != this.frequency)
		{
			RedstoneNetwork network = RedstoneNetwork.getOrCreate(world);
			BlockState state = getBlockState();

			if (isTransmitter && state.get(BlockStateProperties.POWERED))
				network.changeActiveTransmitterFrequency(this.frequency, frequency);
			else if (!isTransmitter)
				network.changeReceiverFrequency(this.frequency, frequency, pos);

			this.frequency = frequency;
			world.notifyBlockUpdate(pos, state, state, 3);
			markDirty();
		}
	}

	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		isTransmitter = compound.getBoolean("isTransmitter");
		frequency = compound.getInt("frequency");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
		compound.putBoolean("isTransmitter", isTransmitter);
		compound.putInt("frequency", frequency);
		return compound;
	}

	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(pos, 3, getUpdateTag());
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		return write(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}

	@Override
	public void onChunkUnloaded()
	{
		if (!world.isRemote && !isTransmitter)
			RedstoneNetwork.getOrCreate(world).removeReceiver(frequency, pos);
	}

	@Override
	public void onLoad()
	{
		if (!world.isRemote && !isTransmitter)
		{
			RedstoneNetwork network = RedstoneNetwork.getOrCreate(world);
			network.addReceiver(frequency, pos, false);
			TaskScheduler.scheduleTask(world, 1, () -> network.updateReceiver(frequency, pos));
		}
	}
}
