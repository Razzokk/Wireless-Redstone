package rzk.wirelessredstone.api;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public interface RedstoneConnectable
{
	/**
	 * For the same purpose as {@link net.minecraft.block.AbstractBlock#emitsRedstonePower(BlockState)}
	 * but differentiating sides.
	 */
	boolean connectsToRedstone(BlockState state, BlockView world, BlockPos pos, @Nullable Direction direction);
}
