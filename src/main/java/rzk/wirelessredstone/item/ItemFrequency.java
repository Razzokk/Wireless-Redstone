package rzk.wirelessredstone.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.client.LangKeys;
import rzk.wirelessredstone.packet.PacketFrequencyItem;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFrequency extends Item
{
	public ItemFrequency()
	{
		super(new Item.Properties().group(WirelessRedstone.ITEM_GROUP_WIRELESS_REDSTONE).maxStackSize(1));
	}

	public void setFrequency(World world, ItemStack stack, int frequency)
	{
		CompoundNBT compound = stack.getOrCreateTag();
		compound.putInt("frequency", frequency);
		stack.setTag(compound);
	}

	public int getFrequency(ItemStack stack)
	{
		return stack.getOrCreateTag().getInt("frequency");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (player.isSneaking())
		{
			if (world.isRemote)
				WirelessRedstone.proxy.openFrequencyGui(getFrequency(stack), new PacketFrequencyItem(hand));
			return ActionResult.resultSuccess(stack);
		}
		return ActionResult.resultPass(stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		ITextComponent textComponent = new TranslationTextComponent(LangKeys.Tooltip.FREQUENCY);
		textComponent.appendText(": ");
		textComponent.applyTextStyle(TextFormatting.GRAY);
		textComponent.appendSibling(new StringTextComponent(Integer.toString(getFrequency(stack))).applyTextStyle(TextFormatting.AQUA));
		tooltip.add(textComponent);
	}
}
