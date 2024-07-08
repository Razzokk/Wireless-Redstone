package rzk.wirelessredstone.misc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

public class WRUtils
{
	public static final int MIN_FREQUENCY = 0;
	public static final int MAX_FREQUENCY = 99999;
	public static final int INVALID_FREQUENCY = -1;

	public static boolean isValidFrequency(int frequency)
	{
		return frequency >= MIN_FREQUENCY && frequency <= MAX_FREQUENCY;
	}

	public static void writeFrequency(NbtCompound nbt, int frequency)
	{
		if (nbt == null || !isValidFrequency(frequency)) return;
		nbt.putInt(NbtKeys.FREQUENCY, frequency);
	}

	public static int readFrequency(NbtCompound nbt)
	{
		if (nbt == null || !nbt.contains(NbtKeys.FREQUENCY)) return INVALID_FREQUENCY;
		return nbt.getInt(NbtKeys.FREQUENCY);
	}

	public static void writeTarget(NbtCompound nbt, BlockPos target)
	{
		if (target == null) return;
		nbt.put(NbtKeys.LINKER_TARGET, NbtHelper.fromBlockPos(target));
	}

	public static BlockPos readTarget(NbtCompound nbt)
	{
		if (nbt == null || !nbt.contains(NbtKeys.LINKER_TARGET)) return null;
		return NbtHelper.toBlockPos(nbt.getCompound(NbtKeys.LINKER_TARGET));
	}

	public static int clamp(int min, int max, int value)
	{
		return Math.min(Math.max(min, value), max);
	}

	public static MutableText frequencyText(int frequency)
	{
		return Text.literal(String.valueOf(frequency)).formatted(Formatting.AQUA);
	}

	public static MutableText positionText(BlockPos pos)
	{
		var x = Text.literal(String.valueOf(pos.getX())).formatted(Formatting.YELLOW);
		var y = Text.literal(String.valueOf(pos.getY())).formatted(Formatting.YELLOW);
		var z = Text.literal(String.valueOf(pos.getZ())).formatted(Formatting.YELLOW);
		return Text.translatable(TranslationKeys.TOOLTIP_POSITION, x, y, z).formatted(Formatting.WHITE);
	}

	public static void appendTeleportCommandIfAllowed(MutableText text, PlayerEntity player, BlockPos pos)
	{
		if (player == null || !player.hasPermissionLevel(2)) return;

		var command = String.format("/tp %d %d %d", pos.getX(), pos.getY() + 1, pos.getZ());
		var click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
		var hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable(TranslationKeys.MESSAGE_TELEPORT));
		text.setStyle(text.getStyle().withClickEvent(click).withHoverEvent(hover));
	}
}
