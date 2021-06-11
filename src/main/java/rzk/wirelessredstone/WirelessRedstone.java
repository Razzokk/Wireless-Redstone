package rzk.wirelessredstone;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.Logger;
import rzk.wirelessredstone.network.PacketHandler;
import rzk.wirelessredstone.proxy.IProxy;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;
import rzk.wirelessredstone.registry.ModRecipes;
import rzk.wirelessredstone.registry.ModTiles;
import rzk.wirelessredstone.util.TaskScheduler;
import rzk.wirelessredstone.util.WRCommand;
import rzk.wirelessredstone.util.WRConfig;
import rzk.wirelessredstone.util.WRCreativeTab;
import rzk.wirelessredstone.util.WREventHandler;
import java.awt.Color;

@Mod(modid = WirelessRedstone.MOD_ID)
public class WirelessRedstone
{
	public static final String MOD_ID = "wirelessredstone";
	public static final CreativeTabs CREATIVE_TAB = new WRCreativeTab();
	public static int freqColor = 0;

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

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		freqColor = Color.decode(WRConfig.freqDisplayColor).getRGB();
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new WRCommand());
	}

	@EventHandler
	public void onServerStop(FMLServerStoppingEvent event)
	{
		TaskScheduler.onServerStop();
	}
}
