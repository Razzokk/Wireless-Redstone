package rzk.wirelessredstone.item;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.generator.language.LanguageBase;
import rzk.wirelessredstone.misc.Config;
import rzk.wirelessredstone.misc.Utils;
import rzk.wirelessredstone.network.PacketHandler;
import rzk.wirelessredstone.network.SnifferHighlightPacket;

import java.util.Iterator;
import java.util.Set;

public class SnifferItem extends FrequencyItem
{
	public SnifferItem(Properties props)
	{
		super(props);
	}

	public void setHighlightedBlocks(long timestamp, ItemStack stack, BlockPos[] coords)
	{
		CompoundTag tag = stack.getOrCreateTag();
		tag.putLong("timestamp", timestamp);

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
				player.displayClientMessage(new TranslatableComponent(LanguageBase.MESSAGE_NO_FREQUENCY).withStyle(ChatFormatting.RED), false);
			return InteractionResultHolder.fail(stack);
		}

		player.getCooldowns().addCooldown(this, 20);

		if (!level.isClientSide)
		{
			RedstoneEther ether = RedstoneEther.get((ServerLevel) level);
			if (ether == null) return InteractionResultHolder.success(stack);

			Set<BlockPos> transmitters = ether.getTransmitters(frequency);
			Component frequencyComponent = new TextComponent(String.valueOf(frequency)).withStyle(ChatFormatting.AQUA);

			if (transmitters.isEmpty())
			{
				player.displayClientMessage(new TranslatableComponent(LanguageBase.MESSAGE_TRANSMITTERS_EMPTY, frequencyComponent), false);
				removeHighlightBlocks(stack);
			}
			else
			{
				Iterator<BlockPos> iterator = transmitters.iterator();
				MutableComponent message = new TranslatableComponent(LanguageBase.MESSAGE_TRANSMITTERS_ACTIVE, frequencyComponent, transmitters.size());
				message.append("\n");

				while (true)
				{
					BlockPos transmitter = iterator.next();
					MutableComponent component = new TextComponent(String.format("[x: %d, y: %d, z: %d]", transmitter.getX(), transmitter.getY(), transmitter.getZ())).withStyle(ChatFormatting.YELLOW);

					if (player.hasPermissions(Commands.LEVEL_GAMEMASTERS))
					{
						ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/tp %d %d %d", transmitter.getX(), transmitter.getY() + 1, transmitter.getZ()));
						HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent(LanguageBase.MESSAGE_TELEPORT));
						component.withStyle(component.getStyle().withClickEvent(click).withHoverEvent(hover));
					}

					message.append(component);

					if (iterator.hasNext()) message.append("\n");
					else break;

					if (message.getString().length() >= 1000)
					{
						message.append("...");
						break;
					}
				}

				player.displayClientMessage(message, false);
				PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
						new SnifferHighlightPacket(level.getGameTime(), hand, transmitters.toArray(BlockPos[]::new)));
			}
		}

		return InteractionResultHolder.success(stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int itemSlot, boolean isSelected)
	{
		if (!isSelected || !level.isClientSide || !stack.hasTag()) return;

		CompoundTag tag = stack.getTag();

		if (level.getGameTime() >= tag.getLong("timestamp") + Config.highlightTimeSeconds * 20L)
			removeHighlightBlocks(stack);
	}
}
