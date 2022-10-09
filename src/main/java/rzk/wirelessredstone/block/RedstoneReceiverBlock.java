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
import rzk.wirelessredstone.blockentity.RedstoneReceiverBlockEntity;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.Config;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class RedstoneReceiverBlock extends RedstoneTransceiverBlock
{
	public RedstoneReceiverBlock(Properties props)
	{
		super(props);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean unknown)
	{
		if (!level.isClientSide && Config.redstoneReceiverStrongPower)
			for (Direction direction : Direction.values())
				level.updateNeighborsAtExceptFromFacing(pos.relative(direction), this, direction.getOpposite());
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
	{
		RedstoneEther ether = RedstoneEther.getOrCreate(level);
		boolean powered = ether.isFrequencyActive(getFrequency(level, pos));

		if (state.getValue(POWERED) != powered)
			level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_ALL);
	}

	@Override
	public boolean isSignalSource(BlockState state)
	{
		return true;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction)
	{
		return state.getValue(POWERED) ? Config.redstoneReceiverSignalStrength : 0;
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction)
	{
		return Config.redstoneReceiverStrongPower ? getSignal(state, blockGetter, pos, direction) : 0;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new RedstoneReceiverBlockEntity(pos, state);
	}
}
