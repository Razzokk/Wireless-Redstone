package rzk.wirelessredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.blockentities.RedstoneReceiverBlockEntity;
import rzk.wirelessredstone.ether.RedstoneEther;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class RedstoneReceiverBlock extends RedstoneTransceiverBlock
{
	public RedstoneReceiverBlock(Properties props)
	{
		super(props);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState newState, boolean unknown)
	{
		WirelessRedstone.LOGGER.debug("onPlace (clientSide: {})", level.isClientSide);
		RedstoneEther ether = RedstoneEther.instance();
		ether.addReceiver(level, getFrequency(level, pos), pos);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean unknown)
	{
		RedstoneEther ether = RedstoneEther.instance();
		ether.removeReceiver(level, getFrequency(level, pos), pos);
		super.onRemove(state, level, pos, newState, unknown);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
	{
		boolean active = RedstoneEther.instance().isFreqActive(getFrequency(level, pos));
		level.setBlock(pos, state.setValue(POWERED, active), Block.UPDATE_ALL);
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

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new RedstoneReceiverBlockEntity(pos, state);
	}
}
