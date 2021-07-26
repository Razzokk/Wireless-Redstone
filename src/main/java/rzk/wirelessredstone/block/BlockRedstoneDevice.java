package rzk.wirelessredstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraftforge.common.util.Constants;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public abstract class BlockRedstoneDevice extends Block
{
	public BlockRedstoneDevice(Properties properties)
	{
		super(properties.isRedstoneConductor((state, reader, pred) -> false));
		registerDefaultState(defaultBlockState().setValue(POWERED, false));
	}

	protected static Direction getDirectionFromPos(BlockPos origin, BlockPos end)
	{
		return Direction.fromNormal(end.getX() - origin.getX(), end.getY() - origin.getY(), end.getZ() - origin.getZ());
	}

	protected abstract boolean isInputSide(BlockState state, Direction side);

	protected abstract boolean isOutputSide(BlockState state, Direction side);

	protected abstract void onInputChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighbour, Direction side);

	protected boolean isPowered(BlockState state)
	{
		return state.hasProperty(POWERED) && state.getValue(POWERED);
	}

	protected int getInputPower(Level world, BlockPos pos, Direction side)
	{
		BlockPos neighbour = pos.relative(side);
		int power = world.getSignal(neighbour, side);

		if (power >= 15)
			return power;

		BlockState neighbourState = world.getBlockState(neighbour);
		return Math.max(power, neighbourState.is(Blocks.REDSTONE_WIRE) ? neighbourState.getValue(RedStoneWireBlock.POWER) : 0);
	}

	protected boolean isGettingPowered(BlockState state, Level world, BlockPos pos, Direction... sides)
	{
		if (sides == null || sides.length == 0)
			return isGettingPowered(state, world, pos, Direction.values());

		for (Direction side : sides)
			if (isInputSide(state, side) && getInputPower(world, pos, side) > 0)
				return true;

		return false;
	}

	protected void updateNeighborsInFront(Level world, BlockPos pos, Direction side)
	{
		BlockPos neighbour = pos.relative(side);
		world.neighborChanged(neighbour, this, pos);
		world.updateNeighborsAtExceptFromFacing(neighbour, this, side.getOpposite());
	}

	protected void updateNeighbours(BlockState state, Level world, BlockPos pos, Direction... sides)
	{
		if (sides == null || sides.length == 0)
		{
			updateNeighbours(state, world, pos, Direction.values());
			return;
		}

		for (Direction side : sides)
			if (isOutputSide(state, side))
				updateNeighborsInFront(world, pos, side);
	}

	protected void setPowered(BlockState state, Level world, BlockPos pos, boolean powered)
	{
		if (state.is(this))
		{
			world.setBlock(pos, state.setValue(POWERED, powered), Constants.BlockFlags.BLOCK_UPDATE);

			if (isSignalSource(state))
				updateNeighbours(state, world, pos);
		}
	}

	protected boolean shouldUpdate(BlockState state, Level world, BlockPos pos)
	{
		return isGettingPowered(state, world, pos) != state.getValue(POWERED);
	}

	@Override
	public boolean isSignalSource(BlockState state)
	{
		return true;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction side)
	{
		return isPowered(state) && isOutputSide(state, side.getOpposite()) ? 15 : 0;
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction side)
	{
		return getSignal(state, blockGetter, pos, side);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighbour, boolean isMoving)
	{
		Direction side = getDirectionFromPos(pos, neighbour);

		if (isInputSide(state, side))
			onInputChanged(state, world, pos, block, neighbour, side);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(POWERED);
	}
}
