package rzk.wirelessredstone.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import rzk.wirelessredstone.RedstoneNetwork;
import rzk.wirelessredstone.registry.ModBlocks;

import javax.annotation.Nullable;

public class TileFrequency extends TileEntity
{
	public static final TileEntityType<TileFrequency> TYPE = TileEntityType.Builder.create(TileFrequency::new, ModBlocks.TRANSMITTER, ModBlocks.RECEIVER).build(null);

	private int frequency;
	private final boolean isTransmitter;

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
			if (isTransmitter)
			{
				if (getBlockState().get(BlockStateProperties.POWERED))
					network.changeActiveTransmitterFrequency(this.frequency, frequency, world);
			}
			else
			{
				network.changeReceiverFrequency(this.frequency, frequency, pos, world);
			}

			this.frequency = frequency;
			world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), 3);
			markDirty();
		}
	}

	@Override
	public void read(CompoundNBT compound)
	{
		super.read(compound);
		frequency = compound.getInt("frequency");
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		super.write(compound);
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
}
