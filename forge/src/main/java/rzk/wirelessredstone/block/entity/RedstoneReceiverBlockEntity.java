package rzk.wirelessredstone.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.WRUtils;

public class RedstoneReceiverBlockEntity extends RedstoneTransceiverBlockEntity
{
	public RedstoneReceiverBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModBlockEntities.REDSTONE_RECEIVER_BLOCK_ENTITY_TYPE.get(), pos, state);
	}

	@Override
	protected void onFrequencyChange(int oldFrequency, int newFrequency)
	{
		if (level.isClientSide) return;
		RedstoneEther ether = RedstoneEther.getOrCreate((ServerLevel) level);
		ether.removeReceiver(worldPosition, oldFrequency);

		if (WRUtils.isValidFrequency(newFrequency))
			ether.addReceiver(level, worldPosition, newFrequency);
	}

	@Override
	public void setLevel(Level pLevel)
	{
		super.setLevel(pLevel);
		if (level.isClientSide) return;

		level.getServer().tell(new TickTask(1, () ->
		{
			RedstoneEther ether = RedstoneEther.getOrCreate((ServerLevel) level);
			ether.addReceiver(level, worldPosition, frequency);
		}));
	}

	@Override
	public void setRemoved()
	{
		if (!level.isClientSide)
		{
			RedstoneEther ether = RedstoneEther.getOrCreate((ServerLevel) level);
			ether.removeReceiver(worldPosition, frequency);
		}
		super.setRemoved();
	}
}
