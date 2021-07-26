package rzk.wirelessredstone.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rzk.wirelessredstone.rsnetwork.Device;

public class ClientScreens
{
	@OnlyIn(Dist.CLIENT)
	public static void openFreqGui(Device device)
	{
		Minecraft.getInstance().setScreen(new ScreenFrequency(device));
	}

	@OnlyIn(Dist.CLIENT)
	public static ScreenConfig openConfigGui(Minecraft mc, Screen screen)
	{
		return new ScreenConfig(screen);
	}
}
