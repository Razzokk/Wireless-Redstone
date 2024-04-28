package rzk.wirelessredstone.item;

import net.minecraft.SharedConstants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.NbtKeys;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.misc.WRUtils;
import rzk.wirelessredstone.registry.ModItems;

import java.util.ArrayList;
import java.util.Set;

public class SnifferItem extends FrequencyItem
{
	public SnifferItem(Settings settings)
	{
		super(settings);
	}

	public static BlockPos[] getHighlightedBlocks(ItemStack stack)
	{
		if (!stack.isOf(ModItems.frequencySniffer)) return null;

		var nbt = stack.getNbt();
		if (nbt == null) return null;

		var list = nbt.getList(NbtKeys.HIGHLIGHTS, NbtElement.COMPOUND_TYPE);
		if (list.isEmpty()) return null;

		var coords = new BlockPos[list.size()];
		for (int i = 0; i < list.size(); ++i)
			coords[i] = NbtHelper.toBlockPos(list.getCompound(i));

		return coords;
	}

	public static void setHighlightedBlocks(long timestamp, ItemStack stack, BlockPos[] coords)
	{
		if (!stack.isOf(ModItems.frequencySniffer)) return;

		var nbt = stack.getOrCreateNbt();
		nbt.putLong(NbtKeys.TIMESTAMP, timestamp);

		var list = new NbtList();
		for (var pos : coords)
			list.add(NbtHelper.fromBlockPos(pos));
		nbt.put(NbtKeys.HIGHLIGHTS, list);
	}

	private static void removeHighlightBlocks(ItemStack stack)
	{
		var nbt = stack.getNbt();
		if (nbt == null) return;

		nbt.remove("timestamp");
		nbt.remove("highlights");
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		if (!context.getPlayer().isSneaking())
			return ActionResult.PASS;
		return super.useOnBlock(context);
	}

	private Text buildActiveTransmittersMessage(PlayerEntity player, Set<BlockPos> transmitters, Text frequencyText)
	{
		var texts = new ArrayList<Text>();
		texts.add(Text.translatable(TranslationKeys.MESSAGE_TRANSMITTERS_ACTIVE, frequencyText, transmitters.size()));

		for (var pos : transmitters)
		{
			if (texts.size() > 20)
			{
				texts.add(Text.literal("..."));
				break;
			}

			var text = WRUtils.positionText(pos);
			texts.add(text);

			if (player.hasPermissionLevel(2))
			{
				var teleportCommand = String.format("/tp %d %d %d", pos.getX(), pos.getY() + 1, pos.getZ());
				ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, teleportCommand);
				HoverEvent hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable(TranslationKeys.MESSAGE_TELEPORT));
				text.setStyle(text.getStyle().withClickEvent(click).withHoverEvent(hover));
			}
		}

		return Texts.join(texts, Text.literal("\n"));
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
				player.sendMessage(Text.translatable(TranslationKeys.MESSAGE_NO_FREQUENCY).formatted(Formatting.RED), true);
			return TypedActionResult.fail(stack);
		}

		player.getItemCooldownManager().set(this, SharedConstants.TICKS_PER_SECOND);
		var result = TypedActionResult.success(stack);
		if (world.isClient) return result;

		var ether = RedstoneEther.get((ServerWorld) world);
		if (ether == null) return result;

		var transmitters = ether.getTransmitters(frequency);
		var frequencyText = WRUtils.frequencyText(frequency);

		if (transmitters.isEmpty())
		{
			player.sendMessage(Text.translatable(TranslationKeys.MESSAGE_TRANSMITTERS_EMPTY, frequencyText));
			removeHighlightBlocks(stack);
		}
		else
		{
			var message = buildActiveTransmittersMessage(player, transmitters, frequencyText);
			player.sendMessage(message);
			WirelessRedstone.PLATFORM.sendSniffer((ServerPlayerEntity) player, world.getTime(), hand, transmitters.toArray(BlockPos[]::new));
		}

		return result;
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
	{
		var nbt = stack.getNbt();
		if (!selected || !world.isClient || nbt == null) return;

		var timeOffset = (long) WRConfig.highlightTimeSeconds * SharedConstants.TICKS_PER_SECOND;
		if (world.getTime() >= nbt.getLong("timestamp") + timeOffset)
			removeHighlightBlocks(stack);
	}
}
