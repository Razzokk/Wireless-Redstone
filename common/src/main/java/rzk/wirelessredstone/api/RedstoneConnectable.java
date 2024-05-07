package rzk.wirelessredstone.api;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.RedstoneView.DIRECTIONS;

public interface RedstoneConnectable
{
	/**
	 * For the same purpose as {@link net.minecraft.block.AbstractBlock#emitsRedstonePower(BlockState)}
	 * but differentiating sides.
	 */
	default boolean connectsToRedstone(BlockState state, BlockView world, BlockPos pos, @Nullable Direction direction)
	{
		return true;
	}

	/**
	 * Determines if the block is receiving redstone power on the given side/direction
	 */
	default boolean isPoweredOnSide(BlockState state, WorldAccess world, BlockPos pos, Direction direction)
	{
		return connectsToRedstone(state, world, pos, direction) && world.isEmittingRedstonePower(pos.offset(direction), direction);
	}

	/**
	 * Determines if the block is receiving redstone power
	 */
	default boolean isReceivingRedstonePower(BlockState state, WorldAccess world, BlockPos pos)
	{
		for (Direction side : DIRECTIONS)
			if (isPoweredOnSide(state, world, pos, side)) return true;
		return false;
	}
}
