package rzk.wirelessredstone.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class Screens
{
	public static void openFrequencyBlockScreen(int frequency, BlockPos pos)
	{
		MinecraftClient.getInstance().setScreen(new FrequencyBlockScreen(frequency, pos));
	}

	public static void openFrequencyItemScreen(int frequency, Hand hand)
	{
		MinecraftClient.getInstance().setScreen(new FrequencyItemScreen(frequency, hand));
	}
}
