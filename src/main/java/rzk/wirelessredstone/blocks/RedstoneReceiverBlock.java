package rzk.wirelessredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.blockentities.RedstoneReceiverBlockEntity;
import rzk.wirelessredstone.config.Config;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.Utils;

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
		if (level.isClientSide) return;
		int frequency = getFrequency(level, pos);

		if (Utils.isValidFrequency(frequency))
			RedstoneEther.getOrCreate((ServerLevel) level).addReceiver(level, pos, frequency);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean unknown)
	{
		if (!level.isClientSide)
		{
			RedstoneEther ether = RedstoneEther.get((ServerLevel) level);
			if (ether != null)
				ether.removeReceiver(pos, getFrequency(level, pos));
		}

		super.onRemove(state, level, pos, newState, unknown);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
	{
		RedstoneEther ether = RedstoneEther.getOrCreate(level);
		boolean active = ether.isFrequencyActive(getFrequency(level, pos));
		setPowered(state, level, pos, active);
	}

	@Override
	public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction)
	{
		if (Config.redstoneReceiverProvidesStrongPower)
			getDirectSignal(state, blockGetter, pos, direction);

		return super.getSignal(state, blockGetter, pos, direction);
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction)
	{
		return state.getValue(POWERED) ? Config.redstoneReceiverOutputPower : 0;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new RedstoneReceiverBlockEntity(pos, state);
	}
}
