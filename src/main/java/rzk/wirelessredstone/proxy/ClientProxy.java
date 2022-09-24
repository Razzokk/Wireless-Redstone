package rzk.wirelessredstone.proxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import rzk.wirelessredstone.client.gui.GuiFrequency;
import rzk.wirelessredstone.client.model.ModModels;
import rzk.wirelessredstone.client.render.FastTESRFrequency;
import rzk.wirelessredstone.client.render.HighlightRenderer;
import rzk.wirelessredstone.network.PacketFrequency;
import rzk.wirelessredstone.tile.TileReceiver;
import rzk.wirelessredstone.tile.TileTransmitter;

public class ClientProxy implements IProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(ModModels.class);
		MinecraftForge.EVENT_BUS.register(HighlightRenderer.class);
		FastTESRFrequency frequencyTESR = new FastTESRFrequency();
		ClientRegistry.bindTileEntitySpecialRenderer(TileTransmitter.class, frequencyTESR);
		ClientRegistry.bindTileEntitySpecialRenderer(TileReceiver.class, frequencyTESR);
	}

	@Override
	public void openFrequencyGui(PacketFrequency packet)
	{
		Minecraft mc = Minecraft.getMinecraft();
		mc.addScheduledTask(() -> mc.displayGuiScreen(new GuiFrequency(packet)));
	}
}
