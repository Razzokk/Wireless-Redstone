package rzk.wirelessredstone.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import rzk.wirelessredstone.blocks.ReceiverBlock;
import rzk.wirelessredstone.blocks.TransmitterBlock;
import rzk.wirelessredstone.ether.Ether;
import rzk.wirelessredstone.registries.ModBlockEntities;

public class WirelessBlockEntity extends BlockEntity
{
	private final String FREQ_STRING = "frequency";
	private int freq = 0;

	public WirelessBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModBlockEntities.WIRELESS_BLOCK_ENTITY.get(), pos, state);
	}

	public int getFreq()
	{
		return freq;
	}

	public void setFreq(int freq)
	{
		if (freq == this.freq) return;

		Ether ether = Ether.instance();

		if (getBlockState().getBlock() instanceof ReceiverBlock)
		{
			ether.removeReceiver(level, this.freq, worldPosition);
			ether.addReceiver(level, freq, worldPosition);
		}
		else if (getBlockState().getBlock() instanceof TransmitterBlock)
		{
			ether.removeTransmitter(level, this.freq, worldPosition);
			ether.addTransmitter(level, freq, worldPosition);
		}

		this.freq = freq;
		setChanged();
	}

	@Override
	public void load(CompoundTag compoundTag)
	{
		super.load(compoundTag);
		freq = compoundTag.getInt(FREQ_STRING);
	}

	@Override
	protected void saveAdditional(CompoundTag compoundTag)
	{
		super.saveAdditional(compoundTag);
		compoundTag.putInt(FREQ_STRING, freq);
	}
}
