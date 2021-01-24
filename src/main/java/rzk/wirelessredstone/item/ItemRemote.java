package rzk.wirelessredstone.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
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
import rzk.wirelessredstone.RedstoneNetwork;
import rzk.wirelessredstone.client.LangKeys;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRemote extends ItemFrequency
{
	public ItemRemote()
	{
		setHasSubtypes(true);
	}

	public static boolean isPowered(ItemStack stack)
	{
		return stack.getMetadata() != 0;
	}

	public static void setPowered(World world, ItemStack stack, boolean powered)
	{
		stack.setItemDamage(powered ? 1 : 0);
		RedstoneNetwork network = RedstoneNetwork.getOrCreate(world);

		if (network != null)
		{
			short frequency = getFrequency(stack);
			if (powered)
				network.addRemote(frequency);
			else
				network.removeRemote(frequency);
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

		if (!world.isRemote)
		{
			if (!isPowered(stack))
				setPowered(world, stack, true);
			player.setActiveHand(hand);
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entityLiving, int timeLeft)
	{
		if (!world.isRemote && isPowered(stack))
			setPowered(world, stack, false);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (!world.isRemote && stack.getItem() instanceof ItemRemote && !isSelected)
			setPowered(world, stack, false);
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
}
