package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.block.entity.ModBlockEntities;
import rzk.wirelessredstone.block.entity.RedstoneTransmitterBlockEntity;

import static net.minecraft.state.property.Properties.POWERED;

public class RedstoneTransmitterBlock extends RedstoneTransceiverBlock
{
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		var world = ctx.getWorld();
		var pos = ctx.getBlockPos();
		var state = super.getPlacementState(ctx);
		return state.with(POWERED, isReceivingRedstonePower(state, world, pos));
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify)
	{
		world.getBlockEntity(pos, ModBlockEntities.redstoneTransmitterBlockEntityType)
			.ifPresent(entity -> entity.onBlockPlaced(state, world, pos));
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
	{
		world.getBlockEntity(pos, ModBlockEntities.redstoneTransmitterBlockEntityType)
			.ifPresent(entity -> entity.onBlockRemoved(state, world, pos));
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	private boolean isReceivingRedstonePower(BlockState state, World world, BlockPos pos)
	{
		for (Direction side : DIRECTIONS)
			if (isSideConnectable(state, world, pos, side) && world.isEmittingRedstonePower(pos.offset(side), side))
				return true;
		return false;
	}

	@Override
	protected void onSideConnectableToggled(BlockState state, World world, BlockPos pos, Direction side)
	{
		var powered = isReceivingRedstonePower(state, world, pos);
		if (state.get(POWERED) != powered) state = state.with(POWERED, powered);
		world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify)
	{
		if (world.isClient) return;
		boolean powered = isReceivingRedstonePower(state, world, pos);
		if (state.get(POWERED) == powered) return;
		world.setBlockState(pos, state.with(POWERED, powered), Block.NOTIFY_LISTENERS);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new RedstoneTransmitterBlockEntity(pos, state);
	}
}
