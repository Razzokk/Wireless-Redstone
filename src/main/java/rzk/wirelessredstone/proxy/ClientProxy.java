package rzk.wirelessredstone.proxy;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rzk.wirelessredstone.client.render.TERFrequency;
import rzk.wirelessredstone.registry.ModTiles;

public class ClientProxy
{
	public static void clientSetup(FMLClientSetupEvent event)
	{
		ClientRegistry.bindTileEntityRenderer(ModTiles.TILE_FREQUENCY.get(), TERFrequency::new);
	}
}
