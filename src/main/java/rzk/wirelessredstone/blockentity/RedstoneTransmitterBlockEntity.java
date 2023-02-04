package rzk.wirelessredstone.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.WRUtils;

public class RedstoneTransmitterBlockEntity extends RedstoneTransceiverBlockEntity
{
    public RedstoneTransmitterBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.REDSTONE_TRANSMITTER_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void onFrequencyChange(int oldFrequency, int newFrequency)
    {
        if (world.isClient || !getCachedState().get(Properties.POWERED)) return;
        RedstoneEther ether = RedstoneEther.getOrCreate((ServerWorld) world);
        ether.removeTransmitter(world, pos, oldFrequency);

        if (WRUtils.isValidFrequency(newFrequency))
            ether.addTransmitter(world, pos, newFrequency);
    }
}
