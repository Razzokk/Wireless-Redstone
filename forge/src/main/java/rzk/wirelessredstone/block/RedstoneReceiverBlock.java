package rzk.wirelessredstone.block;

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
import rzk.wirelessredstone.block.entity.RedstoneReceiverBlockEntity;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.WRConfig;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class RedstoneReceiverBlock extends RedstoneTransceiverBlock
{
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return true;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
	{
		if (!level.isClientSide && WRConfig.redstoneReceiverStrongPower)
			for (Direction direction : Direction.values())
				level.updateNeighborsAtExceptFromFacing(pos.relative(direction), this, direction.getOpposite());
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		RedstoneEther ether = RedstoneEther.getOrCreate(level);
		boolean powered = ether.isFrequencyActive(getFrequency(level, pos));

		if (state.getValue(POWERED) != powered)
			level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_ALL);
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
	{
		return state.getValue(POWERED) ? WRConfig.redstoneReceiverSignalStrength : 0;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
	{
		return WRConfig.redstoneReceiverStrongPower ? getDirectSignal(state, level, pos, direction) : 0;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new RedstoneReceiverBlockEntity(pos, state);
	}
}
