package rzk.wirelessredstone.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.misc.WRUtils;

public abstract class RedstoneTransceiverBlockEntity extends BlockEntity
{
	protected int frequency = WRUtils.INVALID_FREQUENCY;

	public RedstoneTransceiverBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state)
	{
		super(blockEntityType, pos, state);
	}

	protected abstract void onFrequencyChange(int oldFrequency, int newFrequency);

	public int getFrequency()
	{
		return frequency;
	}

	public void setFrequency(int frequency)
	{
		if (frequency == this.frequency) return;
		onFrequencyChange(this.frequency, frequency);
		this.frequency = frequency;
		setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket()
	{
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag()
	{
		CompoundTag nbt = new CompoundTag();
		WRUtils.writeFrequency(nbt, frequency);
		return nbt;
	}

	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		frequency = WRUtils.readFrequency(nbt);
	}

	@Override
	protected void saveAdditional(CompoundTag nbt)
	{
		super.saveAdditional(nbt);
		WRUtils.writeFrequency(nbt, frequency);
	}
}
