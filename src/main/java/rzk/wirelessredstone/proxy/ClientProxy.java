package rzk.wirelessredstone.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rzk.wirelessredstone.client.render.TERFrequency;
import rzk.wirelessredstone.gui.GuiFrequency;
import rzk.wirelessredstone.gui.GuiRemote;
import rzk.wirelessredstone.tile.TileFrequency;

@OnlyIn(Dist.CLIENT)
public class ClientProxy implements IProxy
{
	@Override
	public void clientSetup(FMLClientSetupEvent event)
	{
		ClientRegistry.bindTileEntityRenderer(TileFrequency.TYPE, TERFrequency::new);
	}

	@Override
	public void openFrequencyGui(boolean isTransmitter, BlockPos pos)
	{
		Minecraft.getInstance().displayGuiScreen(new GuiFrequency(isTransmitter, pos));
	}

	@Override
	public void openRemoteGui(int frequency, Hand hand)
	{
		Minecraft.getInstance().displayGuiScreen(new GuiRemote(frequency, hand));
	}
}
