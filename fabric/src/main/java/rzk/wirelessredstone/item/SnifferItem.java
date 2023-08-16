package rzk.wirelessredstone.item;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.misc.WRUtils;
import rzk.wirelessredstone.network.SnifferHighlightPacket;

import java.util.Iterator;
import java.util.Set;

public class SnifferItem extends FrequencyItem
{
	public SnifferItem(Settings settings)
	{
		super(settings);
	}

	public void setHighlightedBlocks(long timestamp, ItemStack stack, BlockPos[] coords)
	{
		NbtCompound nbt = stack.getOrCreateNbt();
		nbt.putLong("timestamp", timestamp);

		NbtList list = new NbtList();
		for (BlockPos pos : coords)
			list.add(NbtHelper.fromBlockPos(pos));
		nbt.put("highlights", list);
	}

	public void removeHighlightBlocks(ItemStack stack)
	{
		if (stack.hasNbt())
		{
			NbtCompound nbt = stack.getNbt();
			nbt.remove("timestamp");
			nbt.remove("highlights");
		}
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		if (!context.getPlayer().isSneaking())
			return ActionResult.PASS;
		return super.useOnBlock(context);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		if (player.isSneaking())
			return super.use(world, player, hand);

		ItemStack stack = player.getStackInHand(hand);
		int frequency = getFrequency(stack);

		if (!WRUtils.isValidFrequency(frequency))
		{
			if (world.isClient)
				player.sendMessage(Text.translatable(TranslationKeys.MESSAGE_NO_FREQUENCY).formatted(Formatting.RED));
			return TypedActionResult.fail(stack);
		}

		player.getItemCooldownManager().set(this, 20);

		if (!world.isClient)
		{
			RedstoneEther ether = RedstoneEther.get((ServerWorld) world);
			if (ether == null) return TypedActionResult.success(stack);

			Set<BlockPos> transmitters = ether.getTransmitters(frequency);
			MutableText frequencyComponent = Text.literal(String.valueOf(frequency)).formatted(Formatting.AQUA);

			if (transmitters.isEmpty())
			{
				player.sendMessage(Text.translatable(TranslationKeys.MESSAGE_TRANSMITTERS_EMPTY, frequencyComponent));
				removeHighlightBlocks(stack);
			}
			else
			{
				Iterator<BlockPos> iterator = transmitters.iterator();
				MutableText message = Text.translatable(TranslationKeys.MESSAGE_TRANSMITTERS_ACTIVE, frequencyComponent, transmitters.size());
				message.append("\n");

				while (true)
				{
					BlockPos transmitter = iterator.next();
					MutableText component = Text.literal(String.format("[x: %d, y: %d, z: %d]", transmitter.getX(), transmitter.getY(), transmitter.getZ())).formatted(Formatting.YELLOW);

					if (player.hasPermissionLevel(2))
					{
						ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/tp %d %d %d", transmitter.getX(), transmitter.getY() + 1, transmitter.getZ()));
						HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable(TranslationKeys.MESSAGE_TELEPORT));
						component.setStyle(component.getStyle().withClickEvent(click).withHoverEvent(hover));
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

				player.sendMessage(message);
				ServerPlayNetworking.send((ServerPlayerEntity) player, new SnifferHighlightPacket(world.getTime(), hand, transmitters.toArray(BlockPos[]::new)));
			}
		}

		return TypedActionResult.success(stack);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
	{
		if (!selected || !world.isClient || !stack.hasNbt()) return;

		NbtCompound nbt = stack.getNbt();

		if (world.getTime() >= nbt.getLong("timestamp") + WRConfig.highlightTimeSeconds * 20L)
			removeHighlightBlocks(stack);
	}
}
