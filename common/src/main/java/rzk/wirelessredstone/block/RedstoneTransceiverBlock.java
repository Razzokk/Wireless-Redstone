package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.api.SideConnectable;
import rzk.wirelessredstone.block.entity.RedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.item.WrenchItem;
import rzk.wirelessredstone.misc.WRUtils;

import static net.minecraft.state.property.Properties.DOWN;
import static net.minecraft.state.property.Properties.EAST;
import static net.minecraft.state.property.Properties.NORTH;
import static net.minecraft.state.property.Properties.POWERED;
import static net.minecraft.state.property.Properties.SOUTH;
import static net.minecraft.state.property.Properties.UP;
import static net.minecraft.state.property.Properties.WEST;

public abstract class RedstoneTransceiverBlock extends Block implements BlockEntityProvider, SideConnectable
{
	public RedstoneTransceiverBlock()
	{
		super(Settings.create()
			.mapColor(MapColor.IRON_GRAY)
			.solidBlock((state, blockGetter, pos) -> false)
			.strength(1.5F, 5.0F)
			.sounds(BlockSoundGroup.METAL));

		setDefaultState(stateManager.getDefaultState()
			.with(POWERED, false)
			.with(UP, true)
			.with(DOWN, true)
			.with(NORTH, true)
			.with(SOUTH, true)
			.with(EAST, true)
			.with(WEST, true));
	}

	public void setFrequency(World world, BlockPos pos, int frequency)
	{
		if (WRUtils.isValidFrequency(frequency) && world.getBlockEntity(pos) instanceof RedstoneTransceiverBlockEntity transceiver)
			transceiver.setFrequency(frequency);
	}

	public int getFrequency(World world, BlockPos pos)
	{
		if (world.getBlockEntity(pos) instanceof RedstoneTransceiverBlockEntity transceiver)
			return transceiver.getFrequency();
		return 0;
	}

	@Override
	public boolean emitsRedstonePower(BlockState state)
	{
		return true;
	}

	@Override
	public boolean isSideConnectable(BlockState state, BlockView world, BlockPos pos, Direction side)
	{
		return switch (side)
		{
			case DOWN -> state.get(DOWN);
			case UP -> state.get(UP);
			case NORTH -> state.get(NORTH);
			case SOUTH -> state.get(SOUTH);
			case WEST -> state.get(WEST);
			case EAST -> state.get(EAST);
		};
	}

	@Override
	public void toggleSideConnectable(BlockState state, World world, BlockPos pos, Direction side)
	{
		var newState = switch (side)
		{
			case DOWN -> state.cycle(DOWN);
			case UP -> state.cycle(UP);
			case NORTH -> state.cycle(NORTH);
			case SOUTH -> state.cycle(SOUTH);
			case WEST -> state.cycle(WEST);
			case EAST -> state.cycle(EAST);
		};

		onSideConnectableToggled(newState, world, pos, side);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		var stack = ctx.getStack();
		var nbt = stack.getNbt();
		var state = getDefaultState();

		if (nbt != null && nbt.contains("connection_state"))
		{
			var connectionState = nbt.getByte("connection_state");
			state = state.with(UP, (connectionState & (1 << Direction.UP.getId())) != 0)
				.with(DOWN, (connectionState & (1 << Direction.DOWN.getId())) != 0)
				.with(NORTH, (connectionState & (1 << Direction.NORTH.getId())) != 0)
				.with(SOUTH, (connectionState & (1 << Direction.SOUTH.getId())) != 0)
				.with(EAST, (connectionState & (1 << Direction.EAST.getId())) != 0)
				.with(WEST, (connectionState & (1 << Direction.WEST.getId())) != 0);
		}

		return state;
	}

	protected abstract void onSideConnectableToggled(BlockState state, World world, BlockPos pos, Direction side);

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		var item = player.getStackInHand(hand).getItem();

		if (item instanceof FrequencyItem || item instanceof WrenchItem)
			return ActionResult.PASS;

		if (!world.isClient)
			WirelessRedstone.PLATFORM.sendFrequencyBlockPacket((ServerPlayerEntity) player, getFrequency(world, pos), pos);

		return ActionResult.SUCCESS;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(POWERED, UP, DOWN, NORTH, SOUTH, EAST, WEST);
	}
}
