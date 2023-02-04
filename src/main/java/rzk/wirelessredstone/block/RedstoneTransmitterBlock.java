package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.blockentity.RedstoneTransmitterBlockEntity;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.WRUtils;

import static net.minecraft.state.property.Properties.POWERED;


public class RedstoneTransmitterBlock extends RedstoneTransceiverBlock
{
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		return getDefaultState().with(POWERED, world.isReceivingRedstonePower(pos));
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify)
	{
		if (world.isClient || !state.get(POWERED)) return;

		int frequency = getFrequency(world, pos);
		if (!WRUtils.isValidFrequency(frequency)) return;

		RedstoneEther ether = RedstoneEther.getOrCreate((ServerWorld) world);
		ether.addTransmitter(world, pos, frequency);
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
	{
		if (!world.isClient && state.get(POWERED))
		{
			int frequency = getFrequency(world, pos);
			if (WRUtils.isValidFrequency(frequency))
			{
				RedstoneEther ether = RedstoneEther.getOrCreate((ServerWorld) world);
				ether.removeTransmitter(world, pos, frequency);
			}
		}

		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify)
	{
		if (world.isClient) return;

		boolean powered = world.isReceivingRedstonePower(pos);
		if (state.get(POWERED) == powered) return;
		world.setBlockState(pos, state.with(POWERED, powered), Block.NOTIFY_LISTENERS);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new RedstoneTransmitterBlockEntity(pos, state);
	}
}
