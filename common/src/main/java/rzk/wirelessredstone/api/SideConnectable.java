package rzk.wirelessredstone.api;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public interface SideConnectable
{
	/**
	 * Check if side of block is connectable to redstone.
	 *
	 * @param side The side of the to check
	 * @return <c>true</c> if the side is connectable, <c>false</c> otherwise
	 */
	boolean isSideConnectable(BlockState state, BlockView world, BlockPos pos, Direction side);

	/**
	 * Toggle side of block to connectable.
	 *
	 * @param side The side of the to set
	 */
	void toggleSideConnectable(BlockState state, World world, BlockPos pos, Direction side);
}
