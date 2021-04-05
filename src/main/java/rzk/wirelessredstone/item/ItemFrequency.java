package rzk.wirelessredstone.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.client.LangKeys;
import rzk.wirelessredstone.network.PacketFrequency;
import rzk.wirelessredstone.network.PacketHandler;
import rzk.wirelessredstone.tile.TileFrequency;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFrequency extends Item
{
	public ItemFrequency()
	{
		setMaxStackSize(1);
	}

	public static short getFrequency(ItemStack stack)
	{
		if (!stack.hasTagCompound())
			return 0;

		return stack.getTagCompound().getShort("frequency");
	}

	public static void setFrequency(ItemStack stack, short frequency)
	{
		NBTTagCompound nbt;

		if (stack.hasTagCompound())
		{
			nbt = stack.getTagCompound();
		}
		else
		{
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}

		nbt.setShort("frequency", frequency);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (!player.isSneaking())
			return super.onItemRightClick(world, player, hand);

		ItemStack stack = player.getHeldItem(hand);

		if (!world.isRemote && stack.getItem() instanceof ItemFrequency)
		{
			boolean extended = player.getEntityData().getBoolean(WirelessRedstone.MOD_ID + ".extended");
			PacketHandler.INSTANCE.sendTo(new PacketFrequency(getFrequency(stack), extended, hand), (EntityPlayerMP) player);
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if (!(world.getBlockState(pos).getBlock() instanceof BlockFrequency))
			return EnumActionResult.PASS;

		if (!world.isRemote)
		{
			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof TileFrequency)
			{
				ItemStack stack = player.getHeldItem(hand);

				if (player.isSneaking())
					setFrequency(stack, ((TileFrequency) tile).getFrequency());
				else
					((TileFrequency) tile).setFrequency(getFrequency(stack));
			}
		}
		return EnumActionResult.SUCCESS;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		tooltip.add(TextFormatting.GRAY + I18n.format(LangKeys.TOOLTIP_FREQUENCY) + ": " + TextFormatting.AQUA + Short.toUnsignedInt(getFrequency(stack)));
	}
}
