package rzk.wirelessredstone.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rzk.wirelessredstone.rsnetwork.Device;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;
import rzk.wirelessredstone.util.LangKeys;
import rzk.wirelessredstone.util.TaskScheduler;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRemote extends ItemFrequency
{
	public static boolean isPowered(ItemStack stack)
	{
		return stack != null && stack.getItem() instanceof ItemRemote && stack.getMetadata() != 0;
	}

	public static void setPowered(World world, ItemStack stack, boolean powered)
	{
		if (stack != null && stack.getItem() instanceof ItemRemote)
		{
			stack.setItemDamage(powered ? 1 : 0);

			if (!world.isRemote)
			{
				RedstoneNetwork network = RedstoneNetwork.get(world);

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
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if (!player.isSneaking())
			return EnumActionResult.FAIL;

		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (player.isSneaking())
			return super.onItemRightClick(world, player, hand);

		ItemStack stack = player.getHeldItem(hand);

		if (!isPowered(stack))
		{
			setPowered(world, stack, true);
			if (!world.isRemote)
				checkIfInHand(world, stack, player, hand);
		}

		if (!world.isRemote)
			player.setActiveHand(hand);

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft)
	{
		if (isPowered(stack))
			setPowered(world, stack, false);

		if (entity instanceof EntityPlayer)
			((EntityPlayer) entity).getCooldownTracker().setCooldown(this, 10);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		super.addInformation(stack, world, tooltip, flag);

		tooltip.add(TextFormatting.GRAY + I18n.format(LangKeys.TOOLTIP_STATE) + ": " + (isPowered(stack) ?
				(TextFormatting.GREEN + I18n.format(LangKeys.TOOLTIP_ON)) : (TextFormatting.DARK_RED + I18n.format(LangKeys.TOOLTIP_OFF))));
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	public void checkIfInHand(World world, ItemStack stack, EntityPlayer player, EnumHand hand)
	{
		if (stack != null && stack.equals(player.getHeldItem(hand)))
			TaskScheduler.scheduleTask(world, 10, () -> checkIfInHand(world, stack, player, hand));
		else
		{
			setPowered(world, stack, false);
			player.getCooldownTracker().setCooldown(this, 10);
		}
	}
}
