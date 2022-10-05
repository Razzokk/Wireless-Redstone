package rzk.wirelessredstone.misc;

import net.minecraft.nbt.CompoundTag;

public class Utils
{
	public static final int MIN_FREQUENCY = 0;
	public static final int MAX_FREQUENCY = 99999;
	public static final int INVALID_FREQUENCY = -1;

	public static boolean isValidFrequency(int frequency)
	{
		return frequency >= MIN_FREQUENCY && frequency <= MAX_FREQUENCY;
	}

	public static void writeFrequency(CompoundTag tag, int frequency)
	{
		if (tag == null || !isValidFrequency(frequency)) return;
		tag.putInt("frequency", frequency);
	}

	public static int readFrequency(CompoundTag tag)
	{
		if (tag == null || !tag.contains("frequency")) return INVALID_FREQUENCY;
		return tag.getInt("frequency");
	}

	public static int clamp(int min, int max, int value)
	{
		return Math.min(Math.max(min, value), max);
	}
}
