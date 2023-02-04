package rzk.wirelessredstone.blockentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
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
        markDirty();
		world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
	}

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket()
    {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt()
    {
        NbtCompound nbt = new NbtCompound();
        WRUtils.writeFrequency(nbt, frequency);
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt)
    {
        super.readNbt(nbt);
        frequency = WRUtils.readFrequency(nbt);
    }

    @Override
    protected void writeNbt(NbtCompound nbt)
    {
        super.writeNbt(nbt);
        WRUtils.writeFrequency(nbt, frequency);
    }
}
