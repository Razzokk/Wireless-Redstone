package rzk.wirelessredstone;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import rzk.wirelessredstone.network.PacketHandler;
import rzk.wirelessredstone.proxy.IProxy;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;
import rzk.wirelessredstone.registry.ModRecipes;
import rzk.wirelessredstone.registry.ModTiles;
import rzk.wirelessredstone.util.WRCreativeTab;
import rzk.wirelessredstone.util.WREventHandler;

@Mod(modid = WirelessRedstone.MOD_ID)
public class WirelessRedstone
{
    public static final String MOD_ID = "wirelessredstone";
    public static final CreativeTabs CREATIVE_TAB = new WRCreativeTab();

    @SidedProxy(clientSide = "rzk.wirelessredstone.proxy.ClientProxy", serverSide = "rzk.wirelessredstone.proxy.ServerProxy")
    public static IProxy proxy;

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        proxy.preInit(event);

        PacketHandler.registerPackets();

        MinecraftForge.EVENT_BUS.register(ModBlocks.class);
        MinecraftForge.EVENT_BUS.register(ModItems.class);
        MinecraftForge.EVENT_BUS.register(ModTiles.class);
        MinecraftForge.EVENT_BUS.register(ModRecipes.class);
        MinecraftForge.EVENT_BUS.register(WREventHandler.class);
    }
}
