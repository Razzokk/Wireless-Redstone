package rzk.wirelessredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.blockentities.P2PRedstoneTransmitterBlockEntity;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class P2PRedstoneTransmitterBlock extends P2PRedstoneTransceiverBlock implements EntityBlock
{
    public P2PRedstoneTransmitterBlock(Properties props)
    {
        super(props);
    }

    public static void linkReceiver(Level level, BlockPos pos, BlockPos receiverPos)
    {
        if (level.getBlockEntity(pos) instanceof P2PRedstoneTransmitterBlockEntity transmitterBlockEntity)
            transmitterBlockEntity.link(receiverPos);
    }

    public static void unlinkReceiver(Level level, BlockPos pos, BlockPos receiverPos)
    {
        if (level.getBlockEntity(pos) instanceof P2PRedstoneTransmitterBlockEntity transmitterBlockEntity)
            transmitterBlockEntity.unlink(receiverPos);
    }

    private void updateLinkedReceivers(LevelAccessor levelAccessor, BlockPos pos, boolean powered)
    {
        if (levelAccessor.getBlockEntity(pos) instanceof P2PRedstoneTransmitterBlockEntity transmitterBlockEntity)
            transmitterBlockEntity.updateAllP2PReceivers(powered);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbour, BlockPos neighbourPos, boolean unknown)
    {
        if (level.isClientSide) return;

        boolean gettingPowered = level.hasNeighborSignal(pos);
        if (gettingPowered == state.getValue(POWERED)) return;

        level.setBlock(pos, state.setValue(POWERED , gettingPowered), 3);
        updateLinkedReceivers(level, pos, gettingPowered);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState newState, boolean unknown)
    {
        if (level.isClientSide) return;

        boolean gettingPowered = level.hasNeighborSignal(pos);
        if (gettingPowered == state.getValue(POWERED)) return;

        level.setBlock(pos, state.setValue(POWERED , gettingPowered), 3);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean unknown)
    {
        if (level.isClientSide) return;

        if (state.getValue(POWERED))
            updateLinkedReceivers(level, pos, false);

        super.onRemove(state, level, pos, newState, unknown);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new P2PRedstoneTransmitterBlockEntity(pos, state);
    }
}
