package rzk.wirelessredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.blockentities.RedstoneTransmitterBlockEntity;
import rzk.wirelessredstone.ether.RedstoneEther;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class RedstoneTransmitterBlock extends RedstoneTransceiverBlock
{
	public RedstoneTransmitterBlock(Properties props)
	{
		super(props);
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState newState, boolean unknown)
	{
		WirelessRedstone.LOGGER.debug("onPlace (clientSide: {}, tile: {})", level.isClientSide, level.getBlockEntity(pos));
		if (level.hasNeighborSignal(pos))
		{
			level.setBlock(pos, state.setValue(POWERED, true), Block.UPDATE_ALL);
			RedstoneEther ether = RedstoneEther.instance();
			ether.addTransmitter(level, getFrequency(level, pos), pos);
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean unknown)
	{
		WirelessRedstone.LOGGER.debug("onRemove (clientSide: {}, tile: {})", level.isClientSide, level.getBlockEntity(pos));
		RedstoneEther ether = RedstoneEther.instance();
		if (state.getValue(POWERED))
			ether.removeTransmitter(level, getFrequency(level, pos), pos);
		super.onRemove(state, level, pos, newState, unknown);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbour, BlockPos neighbourPos, boolean unknown)
	{
		if (level.isClientSide) return;

		boolean powered = state.getValue(POWERED);
		boolean newState = level.hasNeighborSignal(pos);
		if (powered == newState) return;

		level.setBlock(pos, state.setValue(POWERED, newState), Block.UPDATE_ALL);
		RedstoneEther ether = RedstoneEther.instance();
		int freq = getFrequency(level, pos);

		if (newState)
			ether.addTransmitter(level, freq, pos);
		else
			ether.removeTransmitter(level, freq, pos);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new RedstoneTransmitterBlockEntity(pos, state);
	}
}
