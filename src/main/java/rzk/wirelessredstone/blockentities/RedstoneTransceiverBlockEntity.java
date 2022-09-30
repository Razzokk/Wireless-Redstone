package rzk.wirelessredstone.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class RedstoneTransceiverBlockEntity extends BlockEntity
{
    private int freq = 0;

    public RedstoneTransceiverBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state)
    {
        super(blockEntityType, pos, state);
    }

    protected abstract void onFreqChange(int oldFreq, int newFreq);

    public int getFreq()
    {
        return freq;
    }

    public void setFreq(int freq)
    {
        if (freq == this.freq) return;
        onFreqChange(this.freq, freq);
        this.freq = freq;
        setChanged();
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
	}

    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag tag = new CompoundTag();
		tag.putInt("frequency", freq);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void load(CompoundTag compoundTag)
    {
        super.load(compoundTag);
        freq = compoundTag.getInt("frequency");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag)
    {
        super.saveAdditional(compoundTag);
        compoundTag.putInt("frequency", freq);
    }
}
