package rzk.wirelessredstone.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.TickTask;
import net.minecraft.world.level.block.state.BlockState;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.api.IChunkLoadListener;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.registries.ModBlockEntities;

public class RedstoneReceiverBlockEntity extends RedstoneTransceiverBlockEntity implements IChunkLoadListener
{
    public RedstoneReceiverBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntities.REDSTONE_RECEIVER_BLOCK_ENTITY_TYPE.get(), pos, state);
    }

    @Override
    protected void onFreqChange(int oldFreq, int newFreq)
    {
        RedstoneEther ether = RedstoneEther.instance();
        ether.removeReceiver(level, oldFreq, worldPosition);
        ether.addReceiver(level, newFreq, worldPosition);
    }

    @Override
    public void onChunkLoad()
    {
        if (level.isClientSide) return;
        level.getServer().tell(new TickTask(1, () -> level.scheduleTick(worldPosition, getBlockState().getBlock(), 1)));
        WirelessRedstone.LOGGER.debug("onChunkLoad: (level: {}, entity: {})", level, this);
    }
}
