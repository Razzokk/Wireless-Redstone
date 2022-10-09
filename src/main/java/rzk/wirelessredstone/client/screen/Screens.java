package rzk.wirelessredstone.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.fml.IExtensionPoint;

public class Screens
{
	public static void openFrequencyBlockScreen(int frequency, BlockPos pos)
	{
		Minecraft.getInstance().setScreen(new FrequencyBlockScreen(frequency, pos));
	}

	public static void openFrequencyItemScreen(int frequency, InteractionHand hand)
	{
		Minecraft.getInstance().setScreen(new FrequencyItemScreen(frequency, hand));
	}

	public static Screen openConfigScreen(Minecraft mc, Screen parent)
	{
		return new ConfigScreen(parent);
	}
}
