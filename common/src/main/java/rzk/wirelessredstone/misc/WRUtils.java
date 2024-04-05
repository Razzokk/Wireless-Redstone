package rzk.wirelessredstone.misc;

import net.minecraft.nbt.NbtCompound;

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

	public static int clamp(int min, int max, int value)
	{
		return Math.min(Math.max(min, value), max);
	}
}
