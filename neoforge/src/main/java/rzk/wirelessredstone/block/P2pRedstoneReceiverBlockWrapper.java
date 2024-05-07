package rzk.wirelessredstone.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class P2pRedstoneReceiverBlockWrapper extends P2pRedstoneReceiverBlock
{
	@Override
	public boolean canConnectRedstone(BlockState state, BlockView world, BlockPos pos, @Nullable Direction direction)
	{
		return connectsToRedstone(state, world, pos, direction == null ? null : direction.getOpposite());
	}
}
