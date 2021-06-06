package rzk.wirelessredstone.item;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import rzk.wirelessredstone.rsnetwork.Channel;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;
import rzk.wirelessredstone.tile.TileFrequency;
import rzk.wirelessredstone.util.LangKeys;

public class ItemSniffer extends ItemFrequency
{
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
		player.getCooldownTracker().setCooldown(this, 20);

		if (!world.isRemote)
		{
			RedstoneNetwork network = RedstoneNetwork.get(world, false);
			if (network != null)
			{
				short frequency = getFrequency(stack);
				Channel channel = network.getChannel(frequency);
				ITextComponent freqComponent = new TextComponentString(String.valueOf(Short.toUnsignedInt(frequency))).setStyle(new Style().setColor(TextFormatting.YELLOW));

				if (channel == null || channel.getTransmitters().isEmpty())
				{
					player.sendMessage(new TextComponentTranslation(LangKeys.MESSAGE_NO_TRANSMITTERS, freqComponent));
				}
				else
				{
					ITextComponent message = new TextComponentTranslation(LangKeys.MESSAGE_ACTIVE_TRANSMITTERS, freqComponent);
					message.appendText(" ");
					ObjectIterator<BlockPos> iterator = channel.getTransmitters().iterator();

					while (iterator.hasNext())
					{
						BlockPos transmitter = iterator.next();
						ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/tp %d %d %d", transmitter.getX(), transmitter.getY() + 1, transmitter.getZ()));
						HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentTranslation(LangKeys.MESSAGE_TELEPORT));
						Style style = new Style().setClickEvent(click).setHoverEvent(hover).setColor(TextFormatting.AQUA);
						message.appendSibling(new TextComponentString(String.format("[x: %d, y: %d, z: %d]", transmitter.getX(), transmitter.getY(), transmitter.getZ())).setStyle(style));

						if (iterator.hasNext())
							message.appendText(", ");
					}

					player.sendMessage(message);
				}
			}
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}
}
