package rzk.wirelessredstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.block.entity.ModBlockEntities;
import rzk.wirelessredstone.block.entity.RedstoneTransmitterBlockEntity;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class RedstoneTransmitterBlock extends RedstoneTransceiverBlock
{
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		return defaultBlockState().setValue(POWERED, level.hasNeighborSignal(pos));
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston)
	{
		level.getBlockEntity(pos, ModBlockEntities.REDSTONE_TRANSMITTER_BLOCK_ENTITY_TYPE.get())
			.ifPresent(entity -> entity.onBlockPlaced(state, level, pos));
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
	{
		level.getBlockEntity(pos, ModBlockEntities.REDSTONE_TRANSMITTER_BLOCK_ENTITY_TYPE.get())
			.ifPresent(entity -> entity.onBlockRemoved(state, level, pos));
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify)
	{
		if (level.isClientSide) return;
		boolean powered = level.hasNeighborSignal(pos);
		if (state.getValue(POWERED) == powered) return;
		level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_CLIENTS);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new RedstoneTransmitterBlockEntity(pos, state);
	}
}
