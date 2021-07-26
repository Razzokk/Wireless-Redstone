package rzk.wirelessredstone.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import rzk.wirelessredstone.rsnetwork.Channel;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;
import rzk.wirelessredstone.util.LangKeys;
import rzk.wirelessredstone.util.WRConfig;

import java.util.Iterator;
import java.util.Set;

public class ItemSniffer extends ItemFrequency
{
	public void setHighlightedBlocks(Level world, ItemStack stack, int[] coords)
	{
		if (stack.getItem() instanceof ItemSniffer)
		{
			CompoundTag nbt = stack.getOrCreateTag();
			nbt.putLong("timestamp", world.getGameTime());
			nbt.putIntArray("highlight", coords);
		}
	}

	public void removeHighlightBlocks(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemSniffer && stack.hasTag())
		{
			CompoundTag nbt = stack.getOrCreateTag();
			nbt.remove("timestamp");
			nbt.remove("highlight");
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
		player.getCooldowns().addCooldown(this, 20);

		if (!world.isClientSide)
		{
			RedstoneNetwork network = RedstoneNetwork.get((ServerLevel) world);
			if (network != null)
			{
				short frequency = getFrequency(stack);
				Channel channel = network.getChannel(frequency);
				Component freqComponent = new TextComponent(String.valueOf(Short.toUnsignedInt(frequency))).withStyle(ChatFormatting.AQUA);

				if (channel == null || channel.getTransmitters().isEmpty())
				{
					player.sendMessage(new TranslatableComponent(LangKeys.MESSAGE_NO_TRANSMITTERS, freqComponent), player.getUUID());
					removeHighlightBlocks(stack);
				}
				else
				{
					Set<BlockPos> transmitters = channel.getTransmitters();
					Iterator<BlockPos> iterator = transmitters.iterator();
					Component message = new TranslatableComponent(LangKeys.MESSAGE_ACTIVE_TRANSMITTERS, freqComponent, transmitters.size()).append("\n");
					int current = 0;

					while (iterator.hasNext())
					{
						current++;
						BlockPos transmitter = iterator.next();
						ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/tp %d %d %d", transmitter.getX(), transmitter.getY() + 1, transmitter.getZ()));
						HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent(LangKeys.MESSAGE_TELEPORT));
						Style style = Style.EMPTY.withClickEvent(click).withHoverEvent(hover).withColor(ChatFormatting.YELLOW);
						message.getSiblings().add(new TextComponent(String.format("[x: %d, y: %d, z: %d]", transmitter.getX(), transmitter.getY(), transmitter.getZ())).setStyle(style));

						if (iterator.hasNext())
							message.getSiblings().add(new TextComponent("," + (current % 2 == 0 ? '\n' : ' ')));

						if (message.getString().length() >= 1000)
						{
							message.getSiblings().add(new TextComponent("..."));
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
		return InteractionResultHolder.success(stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (isSelected && !world.isClientSide && stack.getItem() instanceof ItemSniffer && stack.hasTag())
		{
			CompoundTag nbt = stack.getOrCreateTag();

			if (world.getGameTime() >= nbt.getLong("timestamp") + WRConfig.snifferHighlightTime * 20L)
				removeHighlightBlocks(stack);
		}
	}
}
