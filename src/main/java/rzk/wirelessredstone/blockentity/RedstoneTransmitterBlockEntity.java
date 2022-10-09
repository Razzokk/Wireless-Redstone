package rzk.wirelessredstone.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.Utils;
import rzk.wirelessredstone.registry.ModBlockEntities;

public class RedstoneTransmitterBlockEntity extends RedstoneTransceiverBlockEntity
{
    public RedstoneTransmitterBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.REDSTONE_TRANSMITTER_BLOCK_ENTITY_TYPE.get(), pos, state);
    }

    @Override
    protected void onFrequencyChange(int oldFrequency, int newFrequency)
    {
        if (level.isClientSide || !getBlockState().getValue(BlockStateProperties.POWERED)) return;
        RedstoneEther ether = RedstoneEther.getOrCreate((ServerLevel) level);
        ether.removeTransmitter(level, worldPosition, oldFrequency);

        if (Utils.isValidFrequency(newFrequency))
            ether.addTransmitter(level, worldPosition, newFrequency);
    }
}
