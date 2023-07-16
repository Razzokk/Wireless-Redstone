package rzk.wirelessredstone.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.WRUtils;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class RedstoneTransmitterBlockEntity extends RedstoneTransceiverBlockEntity
{
	public RedstoneTransmitterBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModBlockEntities.REDSTONE_TRANSMITTER_BLOCK_ENTITY_TYPE.get(), pos, state);
	}

	@Override
	protected void onFrequencyChange(int oldFrequency, int newFrequency)
	{
		if (level.isClientSide || !getBlockState().getValue(BlockStateProperties.POWERED)) return;
		RedstoneEther ether = RedstoneEther.getOrCreate((ServerLevel) level);
		ether.removeTransmitter(level, worldPosition, oldFrequency);

		if (WRUtils.isValidFrequency(newFrequency))
			ether.addTransmitter(level, worldPosition, newFrequency);
	}

	public void onBlockPlaced(BlockState state, Level level, BlockPos pos)
	{
		if (level.isClientSide || !state.getValue(POWERED) || !WRUtils.isValidFrequency(frequency)) return;
		RedstoneEther ether = RedstoneEther.getOrCreate((ServerLevel) level);
		ether.addTransmitter(level, pos, frequency);
	}

	public void onBlockRemoved(BlockState state, Level level, BlockPos pos)
	{
		if (level.isClientSide || !state.getValue(POWERED) || !WRUtils.isValidFrequency(frequency)) return;
		RedstoneEther ether = RedstoneEther.getOrCreate((ServerLevel) level);
		ether.removeTransmitter(level, pos, frequency);
	}
}
