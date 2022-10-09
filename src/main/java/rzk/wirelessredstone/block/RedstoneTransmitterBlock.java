package rzk.wirelessredstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.blockentity.RedstoneTransmitterBlockEntity;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.Utils;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class RedstoneTransmitterBlock extends RedstoneTransceiverBlock
{
	public RedstoneTransmitterBlock(Properties props)
	{
		super(props);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx)
	{
		Level level = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		return defaultBlockState().setValue(POWERED, level.hasNeighborSignal(pos));
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean unknown)
	{
		if (level.isClientSide || !state.getValue(POWERED)) return;

		int frequency = getFrequency(level, pos);
		if (!Utils.isValidFrequency(frequency)) return;

		RedstoneEther ether = RedstoneEther.getOrCreate((ServerLevel) level);
		ether.addTransmitter(level, pos, frequency);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean unknown)
	{
		if (!level.isClientSide && state.getValue(POWERED))
		{
			int frequency = getFrequency(level, pos);
			if (Utils.isValidFrequency(frequency))
			{
				RedstoneEther ether = RedstoneEther.getOrCreate((ServerLevel) level);
				ether.removeTransmitter(level, pos, frequency);
			}
		}

		super.onRemove(state, level, pos, newState, unknown);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbour, BlockPos neighbourPos, boolean unknown)
	{
		if (level.isClientSide) return;

		boolean powered = level.hasNeighborSignal(pos);
		if (state.getValue(POWERED) == powered) return;
		level.setBlock(pos, state.setValue(POWERED, powered), Block.UPDATE_CLIENTS);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new RedstoneTransmitterBlockEntity(pos, state);
	}
}
