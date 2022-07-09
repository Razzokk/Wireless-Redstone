package rzk.wirelessredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rzk.wirelessredstone.ether.Ether;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class ReceiverBlock extends WirelessBlock
{
	public ReceiverBlock(Properties props)
	{
		super(props);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState newState, boolean unknown)
	{
		Ether ether = Ether.instance();
		ether.addReceiver(level, getFreq(level, pos), pos);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean unknown)
	{
		Ether ether = Ether.instance();
		ether.removeReceiver(level, getFreq(level, pos), pos);
		super.onRemove(state, level, pos, newState, unknown);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
	{
		boolean active = Ether.instance().isFreqActive(getFreq(level, pos));
		level.setBlock(pos, state.setValue(POWERED, active), 3);
	}

	@Override
	public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction)
	{
		return state.getValue(POWERED) ? 15 : 0;
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction)
	{
		return state.getValue(POWERED) ? 15 : 0;
	}
}
