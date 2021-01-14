package rzk.wirelessredstone.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import rzk.wirelessredstone.client.model.ModModels;
import rzk.wirelessredstone.client.render.FastTESRFrequency;
import rzk.wirelessredstone.tile.TileFrequency;

public class ClientProxy implements IProxy
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(ModModels.class);
        ClientRegistry.bindTileEntitySpecialRenderer(TileFrequency.class, new FastTESRFrequency());
    }
}
