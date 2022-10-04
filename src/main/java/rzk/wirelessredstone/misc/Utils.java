package rzk.wirelessredstone.misc;

import net.minecraft.nbt.CompoundTag;

public class Utils
{
	public static void writeFrequency(CompoundTag tag, int frequency)
	{
		if (tag == null || frequency == 0) return;
		tag.putInt("frequency", frequency);
	}

	public static int readFrequency(CompoundTag tag)
	{
		if (tag == null) return 0;
		return tag.getInt("frequency");
	}
}
