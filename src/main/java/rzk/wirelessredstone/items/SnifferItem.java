package rzk.wirelessredstone.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import rzk.wirelessredstone.config.Config;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.generators.language.LanguageBase;
import rzk.wirelessredstone.misc.Utils;

import java.util.Iterator;
import java.util.Set;

public class SnifferItem extends FrequencyItem
{
	public SnifferItem(Properties props)
	{
		super(props);
	}

	public void setHighlightedBlocks(Level level, ItemStack stack, Set<BlockPos> coords)
	{
		CompoundTag tag = stack.getOrCreateTag();
		tag.putLong("timestamp", level.getGameTime());

		ListTag list = new ListTag();
		for (BlockPos pos : coords)
			list.add(NbtUtils.writeBlockPos(pos));
		tag.put("highlights", list);
	}

	public void removeHighlightBlocks(ItemStack stack)
	{
		if (stack.hasTag())
		{
			CompoundTag tag = stack.getTag();
			tag.remove("timestamp");
			tag.remove("highlights");
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
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		if (player.isShiftKeyDown())
			return super.use(level, player, hand);

		ItemStack stack = player.getItemInHand(hand);
		int frequency = getFrequency(stack);

		if (!Utils.isValidFrequency(frequency))
		{
			if (level.isClientSide)
				player.displayClientMessage(Component.translatable(LanguageBase.MESSAGE_NO_FREQUENCY).withStyle(ChatFormatting.RED), false);
			return InteractionResultHolder.fail(stack);
		}

		player.getCooldowns().addCooldown(this, 20);

		if (!level.isClientSide)
		{
			RedstoneEther ether = RedstoneEther.get((ServerLevel) level);
			if (ether == null) return InteractionResultHolder.success(stack);

			Set<BlockPos> transmitters = ether.getTransmitters(frequency);
			Component frequencyComponent = Component.literal(String.valueOf(frequency)).withStyle(ChatFormatting.AQUA);

			if (transmitters.isEmpty())
			{
				player.displayClientMessage(Component.translatable(LanguageBase.MESSAGE_TRANSMITTERS_EMPTY, frequencyComponent), false);
				removeHighlightBlocks(stack);
			}
			else
			{
				Iterator<BlockPos> iterator = transmitters.iterator();
				MutableComponent message = Component.translatable(LanguageBase.MESSAGE_TRANSMITTERS_ACTIVE, frequencyComponent, transmitters.size());
				message.append("\n");

				while (true)
				{
					BlockPos transmitter = iterator.next();
					ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/tp %d %d %d", transmitter.getX(), transmitter.getY() + 1, transmitter.getZ()));
					HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable(LanguageBase.MESSAGE_TELEPORT));
					Style style = Style.EMPTY.withClickEvent(click).withHoverEvent(hover).withColor(ChatFormatting.YELLOW);
					message.append(Component.literal(String.format("[x: %d, y: %d, z: %d]", transmitter.getX(), transmitter.getY(), transmitter.getZ())).withStyle(style));

					if (iterator.hasNext()) message.append("\n");
					else break;

					if (message.getString().length() >= 1000)
					{
						message.append("...");
						break;
					}
				}

				player.displayClientMessage(message, false);
				setHighlightedBlocks(level, stack, transmitters);
			}
		}

		return InteractionResultHolder.success(stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected)
	{
		if (!isSelected || level.isClientSide || !stack.hasTag()) return;

		CompoundTag tag = stack.getOrCreateTag();

		if (level.getGameTime() >= tag.getLong("timestamp") + Config.highlightTime * 20L)
			removeHighlightBlocks(stack);
	}
}
