package rzk.wirelessredstone.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import rzk.wirelessredstone.rsnetwork.Channel;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;
import rzk.wirelessredstone.util.LangKeys;
import rzk.wirelessredstone.util.WRConfig;

import java.util.Iterator;
import java.util.Set;

public class ItemSniffer extends ItemFrequency
{
	public void setHighlightedBlocks(World world, ItemStack stack, int[] coords)
	{
		if (stack.getItem() instanceof ItemSniffer)
		{
			NBTTagCompound nbt = stack.getTagCompound();

			if (nbt == null)
			{
				nbt = new NBTTagCompound();
				stack.setTagCompound(nbt);
			}

			nbt.setLong("timestamp", world.getTotalWorldTime());
			nbt.setIntArray("highlight", coords);
		}
	}

	public void removeHighlightBlocks(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemSniffer)
		{
			NBTTagCompound nbt = stack.getTagCompound();

			if (nbt != null)
			{
				nbt.removeTag("timestamp");
				nbt.removeTag("highlight");
			}
		}
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if (!player.isSneaking())
			return EnumActionResult.PASS;

		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (player.isSneaking())
			return super.onItemRightClick(world, player, hand);

		ItemStack stack = player.getHeldItem(hand);
		player.getCooldownTracker().setCooldown(this, 20);

		if (!world.isRemote)
		{
			RedstoneNetwork network = RedstoneNetwork.get(world, false);
			if (network != null)
			{
				short frequency = getFrequency(stack);
				Channel channel = network.getChannel(frequency);
				ITextComponent freqComponent = new TextComponentString(String.valueOf(Short.toUnsignedInt(frequency))).setStyle(new Style().setColor(TextFormatting.AQUA));

				if (channel == null || channel.getTransmitters().isEmpty())
				{
					player.sendMessage(new TextComponentTranslation(LangKeys.MESSAGE_NO_TRANSMITTERS, freqComponent));
					removeHighlightBlocks(stack);
				}
				else
				{
					Set<BlockPos> transmitters = channel.getTransmitters();
					Iterator<BlockPos> iterator = transmitters.iterator();
					ITextComponent message = new TextComponentTranslation(LangKeys.MESSAGE_ACTIVE_TRANSMITTERS, freqComponent, transmitters.size());
					message.appendText("\n");
					int current = 0;

					while (iterator.hasNext())
					{
						current++;
						BlockPos transmitter = iterator.next();
						ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/tp %d %d %d", transmitter.getX(), transmitter.getY() + 1, transmitter.getZ()));
						HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation(LangKeys.MESSAGE_TELEPORT));
						Style style = new Style().setClickEvent(click).setHoverEvent(hover).setColor(TextFormatting.YELLOW);
						message.appendSibling(new TextComponentString(String.format("[x: %d, y: %d, z: %d]", transmitter.getX(), transmitter.getY(), transmitter.getZ())).setStyle(style));

						if (iterator.hasNext())
							message.appendText("," + (current % 2 == 0 ? '\n' : ' '));

						if (message.getUnformattedText().length() >= 1000)
						{
							message.appendText("...");
							break;
						}
					}

					player.sendMessage(message);
					int[] coords = new int[transmitters.size() * 3];
					current = 0;

					for (BlockPos transmitter : transmitters)
					{
						if (world.isBlockLoaded(transmitter))
						{
							coords[current] = transmitter.getX();
							coords[++current] = transmitter.getY();
							coords[++current] = transmitter.getZ();
							current++;
						}
					}

					setHighlightedBlocks(world, stack, coords);
				}
			}
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (isSelected && !world.isRemote && stack.getItem() instanceof ItemSniffer && stack.hasTagCompound())
		{
			NBTTagCompound nbt = stack.getTagCompound();
			long timestamp = nbt.getLong("timestamp");

			if (world.getTotalWorldTime() >= timestamp + WRConfig.snifferHighlightTime * 20L)
				removeHighlightBlocks(stack);
		}
	}
}
