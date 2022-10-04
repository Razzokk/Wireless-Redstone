package rzk.wirelessredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.blockentities.RedstoneTransmitterBlockEntity;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.Utils;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class RedstoneTransmitterBlock extends RedstoneTransceiverBlock
{
	public RedstoneTransmitterBlock(Properties props)
	{
		super(props);
	}

	@Override
	public void setPowered(BlockState state, Level level, BlockPos pos, boolean powered)
	{
		if (state.getValue(POWERED) == powered) return;
		super.setPowered(state, level, pos, powered);

		int frequency = getFrequency(level, pos);
		if (!Utils.isValidFrequency(frequency)) return;

		RedstoneEther ether = RedstoneEther.getOrCreate((ServerLevel) level);

		if (powered)
			ether.addTransmitter(level, pos, frequency);
		else
			ether.removeTransmitter(level, pos, frequency);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState newState, boolean unknown)
	{
		if (!level.isClientSide && level.hasNeighborSignal(pos))
			setPowered(state, level, pos, true);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean unknown)
	{
		if (!level.isClientSide)
		{
			RedstoneEther ether = RedstoneEther.get((ServerLevel) level);
			if (ether != null)
				ether.removeTransmitter(level, pos, getFrequency(level, pos));
		}

		super.onRemove(state, level, pos, newState, unknown);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbour, BlockPos neighbourPos, boolean unknown)
	{
		if (level.isClientSide) return;
		setPowered(state, level, pos, level.hasNeighborSignal(pos));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new RedstoneTransmitterBlockEntity(pos, state);
	}
}
