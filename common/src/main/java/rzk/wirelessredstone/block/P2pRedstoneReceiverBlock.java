package rzk.wirelessredstone.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import rzk.wirelessredstone.misc.WRConfig;

import static net.minecraft.state.property.Properties.POWERED;

public class P2pRedstoneReceiverBlock extends P2pRedstoneTransceiverBlock
{
	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction)
	{
		return state.get(POWERED) && connectsToRedstone(state, world, pos, direction) ?
			WRConfig.redstoneReceiverSignalStrength : 0;
	}

	@Override
	public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction)
	{
		return WRConfig.redstoneReceiverStrongPower ? getWeakRedstonePower(state, world, pos, direction) : 0;
	}
}
