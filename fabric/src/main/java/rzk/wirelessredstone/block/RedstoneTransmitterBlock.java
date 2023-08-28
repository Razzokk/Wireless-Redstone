package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.block.entity.ModBlockEntities;
import rzk.wirelessredstone.block.entity.RedstoneTransmitterBlockEntity;

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
		world.getBlockEntity(pos, ModBlockEntities.REDSTONE_TRANSMITTER_BLOCK_ENTITY)
			.ifPresent(entity -> entity.onBlockPlaced(state, world, pos));
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
	{
		world.getBlockEntity(pos, ModBlockEntities.REDSTONE_TRANSMITTER_BLOCK_ENTITY)
			.ifPresent(entity -> entity.onBlockRemoved(state, world, pos));
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
