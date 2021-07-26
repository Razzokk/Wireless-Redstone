package rzk.wirelessredstone.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import rzk.wirelessredstone.rsnetwork.Device;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;
import rzk.wirelessredstone.util.LangKeys;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRemote extends ItemFrequency
{
	public static boolean isPowered(ItemStack stack)
	{
		return stack != null && stack.getItem() instanceof ItemRemote && stack.getOrCreateTag().getBoolean("powered");
	}

	public static void setPowered(Level world, ItemStack stack, boolean powered)
	{
		if (stack != null && stack.getItem() instanceof ItemRemote)
		{
			stack.getOrCreateTag().putBoolean("powered", powered);

			if (!world.isClientSide)
			{
				RedstoneNetwork network = RedstoneNetwork.get((ServerLevel) world);

				if (network != null)
				{
					Device device = Device.createRemote(getFrequency(stack), null);

					if (powered)
						network.addDevice(device);
					else
						network.removeDevice(device);
				}
			}
		}
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
	{
		if (!context.getPlayer().isShiftKeyDown())
			return InteractionResult.PASS;

		return super.onItemUseFirst(stack, context);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		if (player.isShiftKeyDown())
			return super.use(world, player, hand);

		ItemStack stack = player.getItemInHand(hand);

		if (!isPowered(stack))
			setPowered(world, stack, true);

		if (!world.isClientSide)
			player.startUsingItem(hand);

		return InteractionResultHolder.success(stack);
	}

	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft)
	{
		if (isPowered(stack))
			setPowered(world, stack, false);

		if (entity instanceof Player player)
			player.getCooldowns().addCooldown(this, 10);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (!isSelected && !world.isClientSide && entity instanceof Player player && isPowered(stack))
		{
			setPowered(world, stack, false);
			player.getCooldowns().addCooldown(this, 10);
		}
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag tooltipFlag)
	{
		super.appendHoverText(stack, world, list, tooltipFlag);

		boolean powered = isPowered(stack);
		Component state = new TranslatableComponent(powered ? LangKeys.TOOLTIP_ON : LangKeys.TOOLTIP_OFF).withStyle(powered ? ChatFormatting.GREEN : ChatFormatting.DARK_RED);
		list.add(new TranslatableComponent(LangKeys.TOOLTIP_STATE, state).withStyle(ChatFormatting.GRAY));
	}
}
