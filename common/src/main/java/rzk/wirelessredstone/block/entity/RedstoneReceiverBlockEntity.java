package rzk.wirelessredstone.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.api.ChunkLoadListener;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.WRUtils;
import rzk.wirelessredstone.registry.ModBlockEntities;

public class RedstoneReceiverBlockEntity extends RedstoneTransceiverBlockEntity implements ChunkLoadListener
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
	public void onChunkLoad(ServerWorld world)
	{
		var ether = RedstoneEther.getOrCreate(world);
		ether.addReceiver(world, pos, frequency);
	}

	@Override
	public void onChunkUnload(ServerWorld world)
	{
		var ether = RedstoneEther.get(world);
		if (ether == null) return;
		ether.removeReceiver(pos, frequency);
	}
}
