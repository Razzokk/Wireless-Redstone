package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.block.entity.P2pRedstoneTransmitterBlockEntity;
import rzk.wirelessredstone.item.LinkerItem;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRUtils;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

import java.util.List;

import static net.minecraft.state.property.Properties.POWERED;
import static rzk.wirelessredstone.misc.WRProperties.LINKED;

public class P2pRedstoneTransmitterBlock extends P2pRedstoneTransceiverBlock implements BlockEntityProvider
{
	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		var world = ctx.getWorld();
		var pos = ctx.getBlockPos();
		var state = getDefaultState();
		var target = WRUtils.readTarget(BlockItem.getBlockEntityNbt(ctx.getStack()));

		return state
			.with(POWERED, isReceivingRedstonePower(state, world, pos))
			.with(LINKED, target != null);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify)
	{
		if (!state.get(POWERED)) return;
		world.getBlockEntity(pos, ModBlockEntities.p2pRedstoneTransmitterBlockEntityType)
			.ifPresent(entity -> entity.setTargetState(true));
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
	{
		if (state.getBlock() != newState.getBlock())
		{
			world.getBlockEntity(pos, ModBlockEntities.p2pRedstoneTransmitterBlockEntityType)
				.ifPresent(P2pRedstoneTransmitterBlockEntity::unlink);
		}
		else if (state.get(POWERED))
		{
			world.getBlockEntity(pos, ModBlockEntities.p2pRedstoneTransmitterBlockEntityType)
				.ifPresent(entity -> entity.setTargetState(false));
		}

		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if (player.isSneaking()) return ActionResult.PASS;

		var stack = player.getStackInHand(hand);
		if (!stack.isOf(ModItems.linker)) return ActionResult.PASS;

		var target = LinkerItem.getTarget(stack);
		if (target == null) return ActionResult.PASS;

		var targetState = world.getBlockState(target);
		if (!targetState.isOf(ModBlocks.p2pRedstoneReceiver))
		{
			if (!world.isClient)
			{
				var receiverTranslated = Text
					.translatable(ModBlocks.p2pRedstoneReceiver.getTranslationKey())
					.formatted(Formatting.AQUA);
				var text = Text.translatable(TranslationKeys.MESSAGE_P2P_NO_RECEIVER, receiverTranslated);
				player.sendMessage(text);
			}

			return ActionResult.FAIL;
		}

		var blockEntity = world.getBlockEntity(pos);
		if (!(blockEntity instanceof P2pRedstoneTransmitterBlockEntity p2pEntity)) return ActionResult.PASS;

		p2pEntity.link(target);

		if (!world.isClient)
		{
			var targetText = WRUtils.positionText(pos);
			WRUtils.appendTeleportCommandIfAllowed(targetText, player, target);

			var text = Text.translatable(TranslationKeys.MESSAGE_P2P_LINKED, targetText);
			player.sendMessage(text);
		}

		return ActionResult.SUCCESS;
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify)
	{
		if (world.isClient) return;
		var powered = isReceivingRedstonePower(state, world, pos);

		if (state.get(POWERED) == powered) return;
		world.setBlockState(pos, state.with(POWERED, powered), NOTIFY_LISTENERS);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state)
	{
		return new P2pRedstoneTransmitterBlockEntity(pos, state);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options)
	{
		var target = WRUtils.readTarget(BlockItem.getBlockEntityNbt(stack));
		if (target == null) return;

		var targetText = WRUtils.positionText(target);
		tooltip.add(Text.translatable(TranslationKeys.TOOLTIP_TARGET, targetText).formatted(Formatting.GRAY));
	}
}
