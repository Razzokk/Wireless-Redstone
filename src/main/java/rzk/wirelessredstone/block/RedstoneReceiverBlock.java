package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.block.entity.RedstoneReceiverBlockEntity;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.WRConfig;

import static net.minecraft.state.property.Properties.POWERED;

public class RedstoneReceiverBlock extends RedstoneTransceiverBlock
{
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify)
	{
		if (!world.isClient && WRConfig.redstoneReceiverStrongPower)
			for (Direction direction : Direction.values())
				world.updateNeighborsExcept(pos.offset(direction), this, direction.getOpposite());
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		RedstoneEther ether = RedstoneEther.getOrCreate(world);
		boolean powered = ether.isFrequencyActive(getFrequency(world, pos));

		if (state.get(POWERED) != powered)
			world.setBlockState(pos, state.with(POWERED, powered), Block.NOTIFY_ALL);
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction)
	{
		return state.get(POWERED) ? WRConfig.redstoneReceiverSignalStrength : 0;
	}

	@Override
	public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction)
	{
		return WRConfig.redstoneReceiverStrongPower ? getWeakRedstonePower(state, world, pos, direction) : 0;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new RedstoneReceiverBlockEntity(pos, state);
	}
}
