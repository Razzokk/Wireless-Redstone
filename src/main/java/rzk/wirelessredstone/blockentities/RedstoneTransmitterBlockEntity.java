package rzk.wirelessredstone.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.registries.ModBlockEntities;

public class RedstoneTransmitterBlockEntity extends RedstoneTransceiverBlockEntity
{
    public RedstoneTransmitterBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.REDSTONE_TRANSMITTER_BLOCK_ENTITY_TYPE.get(), pos, state);
    }

    @Override
    protected void onFreqChange(int oldFreq, int newFreq)
    {
        RedstoneEther ether = RedstoneEther.instance();
        ether.removeTransmitter(level, oldFreq, worldPosition);
        ether.addTransmitter(level, newFreq, worldPosition);
    }
}
