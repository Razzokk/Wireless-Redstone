package rzk.wirelessredstone.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.api.IChunkLoadListener;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.Utils;
import rzk.wirelessredstone.registries.ModBlockEntities;
import rzk.wirelessredstone.registries.ModBlocks;

public class RedstoneReceiverBlockEntity extends RedstoneTransceiverBlockEntity implements IChunkLoadListener
{
    public RedstoneReceiverBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.REDSTONE_RECEIVER_BLOCK_ENTITY_TYPE.get(), pos, state);
    }

    @Override
    protected void onFrequencyChange(int oldFrequency, int newFrequency)
    {
        if (level.isClientSide) return;
        RedstoneEther ether = RedstoneEther.getOrCreate((ServerLevel) level);
        ether.removeReceiver(worldPosition, oldFrequency);

        if (Utils.isValidFrequency(newFrequency))
            ether.addReceiver(level, worldPosition, newFrequency);
    }

    @Override
    public void onChunkLoad()
    {
        if (level.isClientSide) return;
        level.getServer().tell(new TickTask(1, () -> level.scheduleTick(worldPosition, ModBlocks.REDSTONE_RECEIVER.get(), 1)));
    }
}
