package rzk.wirelessredstone.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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

	public static void setPowered(World world, ItemStack stack, boolean powered)
	{
		if (stack != null && stack.getItem() instanceof ItemRemote)
		{
			stack.getOrCreateTag().putBoolean("powered", powered);

			if (!world.isClientSide)
			{
				RedstoneNetwork network = RedstoneNetwork.get((ServerWorld) world);

				if (network != null)
				{
					Device device = Device.createRemote(getFrequency(stack));

					if (powered)
						network.addDevice(device);
					else
						network.removeDevice(device);
				}
			}
		}
	}

	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
	{
		if (!context.getPlayer().isShiftKeyDown())
			return ActionResultType.PASS;

		return super.onItemUseFirst(stack, context);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		if (player.isShiftKeyDown())
			return super.use(world, player, hand);

		ItemStack stack = player.getItemInHand(hand);

		if (!isPowered(stack))
			setPowered(world, stack, true);

		if (!world.isClientSide)
			player.startUsingItem(hand);

		return ActionResult.success(stack);
	}

	@Override
	public void releaseUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft)
	{
		if (isPowered(stack))
			setPowered(world, stack, false);

		if (entity instanceof PlayerEntity)
			((PlayerEntity) entity).getCooldowns().addCooldown(this, 10);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag tooltipFlag)
	{
		super.appendHoverText(stack, world, list, tooltipFlag);

		boolean powered = isPowered(stack);
		ITextComponent state = new TranslationTextComponent(powered ? LangKeys.TOOLTIP_ON : LangKeys.TOOLTIP_OFF).withStyle(powered ? TextFormatting.GREEN : TextFormatting.DARK_RED);
		list.add(new TranslationTextComponent(LangKeys.TOOLTIP_STATE, state).withStyle(TextFormatting.GRAY));
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (!isSelected && !world.isClientSide && entity instanceof PlayerEntity && isPowered(stack))
		{
			setPowered(world, stack, false);
			((PlayerEntity) entity).getCooldowns().addCooldown(this, 10);
		}
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 72000;
	}
}
