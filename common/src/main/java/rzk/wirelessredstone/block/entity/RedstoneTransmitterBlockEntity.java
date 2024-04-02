package rzk.wirelessredstone.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.WRUtils;

import static net.minecraft.state.property.Properties.POWERED;

public class RedstoneTransmitterBlockEntity extends RedstoneTransceiverBlockEntity
{
	public RedstoneTransmitterBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModBlockEntities.redstoneTransmitterBlockEntityType, pos, state);
	}

	@Override
	protected void onFrequencyChange(int oldFrequency, int newFrequency)
	{
		if (world.isClient || !getCachedState().get(Properties.POWERED)) return;
		RedstoneEther ether = RedstoneEther.getOrCreate((ServerWorld) world);
		ether.removeTransmitter(world, pos, oldFrequency);

		if (WRUtils.isValidFrequency(newFrequency))
			ether.addTransmitter(world, pos, newFrequency);
	}

	public void onBlockPlaced(BlockState state, World world, BlockPos pos)
	{
		if (world.isClient || !state.get(POWERED) || !WRUtils.isValidFrequency(frequency)) return;
		RedstoneEther ether = RedstoneEther.getOrCreate((ServerWorld) world);
		ether.addTransmitter(world, pos, frequency);
	}

	public void onBlockRemoved(BlockState state, World world, BlockPos pos)
	{
		if (world.isClient || !state.get(POWERED) || !WRUtils.isValidFrequency(frequency)) return;
		RedstoneEther ether = RedstoneEther.getOrCreate((ServerWorld) world);
		ether.removeTransmitter(world, pos, frequency);
	}

	@Override
	public void toggleConnectable(Direction side)
	{
		super.toggleConnectable(side);
		var state = getCachedState();

		if (!state.get(POWERED))
		{
			if (isConnectable(side) && world.isEmittingRedstonePower(pos.offset(side), side))
				world.setBlockState(pos, state.with(POWERED, true));
			return;
		}

		for (Direction dir : Direction.values())
		{
			if (dir == side) continue;
			if (isConnectable(dir) && world.isEmittingRedstonePower(pos.offset(dir), dir))
				return;
		}

		world.setBlockState(pos, state.with(POWERED, false));
	}
}
