package rzk.wirelessredstone.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.rsnetwork.Device;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;

import javax.annotation.Nullable;

public abstract class TileFrequency extends TileEntity implements Device.Block
{
	private short frequency;

	public TileFrequency(TileEntityType<?> tileType)
	{
		super(tileType);
		frequency = 0;
	}

	@Override
	public short getFrequency()
	{
		return frequency;
	}

	public void setFrequency(short frequency)
	{
		if (this.frequency != frequency && !level.isClientSide)
		{
			RedstoneNetwork network = RedstoneNetwork.get((ServerWorld) level);
			BlockState state = level.getBlockState(worldPosition);

			if (!(isTransmitter() && state.is(ModBlocks.redstoneTransmitter) && !state.getValue(BlockStateProperties.POWERED)))
				network.changeDeviceFrequency(this, frequency);

			this.frequency = frequency;
			level.sendBlockUpdated(worldPosition, state, state, Constants.BlockFlags.BLOCK_UPDATE);
			setChanged();
		}
	}

	@Override
	public BlockPos getFreqPos()
	{
		return worldPosition;
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		return save(new CompoundNBT());
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(worldPosition, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		load(level.getBlockState(pkt.getPos()), pkt.getTag());
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);
		frequency = nbt.getShort("frequency");
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt)
	{
		super.save(nbt);
		nbt.putShort("frequency", frequency);
		return nbt;
	}
}
