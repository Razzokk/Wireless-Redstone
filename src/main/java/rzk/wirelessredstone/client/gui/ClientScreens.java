package rzk.wirelessredstone.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rzk.wirelessredstone.rsnetwork.Device;

public class ClientScreens
{
	@OnlyIn(Dist.CLIENT)
	public static void openFreqGui(Device device)
	{
		Minecraft.getInstance().setScreen(new GuiFrequency(device));
	}
}
