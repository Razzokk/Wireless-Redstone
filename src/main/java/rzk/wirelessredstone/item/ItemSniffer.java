package rzk.wirelessredstone.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
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
			CompoundNBT nbt = stack.getOrCreateTag();
			nbt.putLong("timestamp", world.getGameTime());
			nbt.putIntArray("highlight", coords);
		}
	}

	public void removeHighlightBlocks(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemSniffer && stack.hasTag())
		{
			CompoundNBT nbt = stack.getOrCreateTag();
			nbt.remove("timestamp");
			nbt.remove("highlight");
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
		player.getCooldowns().addCooldown(this, 20);

		if (!world.isClientSide)
		{
			RedstoneNetwork network = RedstoneNetwork.get((ServerWorld) world);
			if (network != null)
			{
				short frequency = getFrequency(stack);
				Channel channel = network.getChannel(frequency);
				ITextComponent freqComponent = new StringTextComponent(String.valueOf(Short.toUnsignedInt(frequency))).withStyle(TextFormatting.AQUA);

				if (channel == null || channel.getTransmitters().isEmpty())
				{
					player.sendMessage(new TranslationTextComponent(LangKeys.MESSAGE_NO_TRANSMITTERS, freqComponent), player.getUUID());
					removeHighlightBlocks(stack);
				}
				else
				{
					Set<BlockPos> transmitters = channel.getTransmitters();
					Iterator<BlockPos> iterator = transmitters.iterator();
					ITextComponent message = new TranslationTextComponent(LangKeys.MESSAGE_ACTIVE_TRANSMITTERS, freqComponent, transmitters.size());
					message.getSiblings().add(new StringTextComponent("\n"));
					int current = 0;

					while (iterator.hasNext())
					{
						current++;
						BlockPos transmitter = iterator.next();
						ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/tp %d %d %d", transmitter.getX(), transmitter.getY() + 1, transmitter.getZ()));
						HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent(LangKeys.MESSAGE_TELEPORT));
						Style style = Style.EMPTY.withClickEvent(click).withHoverEvent(hover).withColor(TextFormatting.YELLOW);
						message.getSiblings().add(new StringTextComponent(String.format("[x: %d, y: %d, z: %d]", transmitter.getX(), transmitter.getY(), transmitter.getZ())).setStyle(style));

						if (iterator.hasNext())
							message.getSiblings().add(new StringTextComponent("," + (current % 2 == 0 ? '\n' : ' ')));

						if (message.getString().length() >= 1000)
						{
							message.getSiblings().add(new StringTextComponent("..."));
							break;
						}
					}

					player.sendMessage(message, player.getUUID());
					int[] coords = new int[transmitters.size() * 3];
					current = 0;

					for (BlockPos transmitter : transmitters)
					{
						if (world.isLoaded(transmitter))
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
		return ActionResult.success(stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (isSelected && !world.isClientSide && stack.getItem() instanceof ItemSniffer && stack.hasTag())
		{
			CompoundNBT nbt = stack.getOrCreateTag();

			if (world.getGameTime() >= nbt.getLong("timestamp") + WRConfig.snifferHighlightTime * 20L)
				removeHighlightBlocks(stack);
		}
	}
}
