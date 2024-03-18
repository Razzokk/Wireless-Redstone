package rzk.wirelessredstone.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.screen.FrequencyBlockScreen;
import rzk.wirelessredstone.screen.FrequencyItemScreen;

public class ModScreens
{
	public static void openBlockFrequencyScreen(int frequency, BlockPos pos)
	{
		MinecraftClient.getInstance().setScreen(new FrequencyBlockScreen(frequency, pos));
	}

	public static void openItemFrequencyScreen(int frequency, Hand hand)
	{
		MinecraftClient.getInstance().setScreen(new FrequencyItemScreen(frequency, hand));
	}

	public static Screen getConfigScreen(MinecraftClient minecraft, Screen parent)
	{
		return ConfigScreen.get(parent);
	}
}
