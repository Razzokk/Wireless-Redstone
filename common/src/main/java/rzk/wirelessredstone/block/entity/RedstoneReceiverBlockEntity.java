package rzk.wirelessredstone.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.server.ServerTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.WRUtils;

public class RedstoneReceiverBlockEntity extends RedstoneTransceiverBlockEntity
{
	public RedstoneReceiverBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModBlockEntities.redstoneReceiverBlockEntityType, pos, state);
	}

	@Override
	protected void onFrequencyChange(int oldFrequency, int newFrequency)
	{
		if (world.isClient) return;
		RedstoneEther ether = RedstoneEther.getOrCreate((ServerWorld) world);
		ether.removeReceiver(pos, oldFrequency);

		if (WRUtils.isValidFrequency(newFrequency))
			ether.addReceiver(world, pos, newFrequency);
	}

	@Override
	public void setWorld(World world)
	{
		super.setWorld(world);
		if (world.isClient) return;

		world.getServer().send(new ServerTask(1, () ->
		{
			RedstoneEther ether = RedstoneEther.getOrCreate((ServerWorld) world);
			ether.addReceiver(world, pos, frequency);
		}));
	}

	@Override
	public void markRemoved()
	{
		if (!world.isClient)
		{
			RedstoneEther ether = RedstoneEther.getOrCreate((ServerWorld) world);
			ether.removeReceiver(pos, frequency);
		}
		super.markRemoved();
	}
}
