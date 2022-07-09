package rzk.wirelessredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import rzk.wirelessredstone.ether.Ether;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class TransmitterBlock extends WirelessBlock
{
	public TransmitterBlock(Properties props)
	{
		super(props);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean unknown)
	{
		Ether ether = Ether.instance();
		ether.removeTransmitter(level, getFreq(level, pos), pos);
		super.onRemove(state, level, pos, newState, unknown);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighbour, BlockPos neighbourPos, boolean unknown)
	{
		if (level.isClientSide) return;

		boolean powered = state.getValue(POWERED);
		boolean newState = level.hasNeighborSignal(pos);
		if (powered == newState) return;

		level.setBlock(pos, state.setValue(POWERED, newState), 3);
		Ether ether = Ether.instance();
		int freq = getFreq(level, pos);

		if (newState)
			ether.addTransmitter(level, freq, pos);
		else
			ether.removeTransmitter(level, freq, pos);
	}
}
