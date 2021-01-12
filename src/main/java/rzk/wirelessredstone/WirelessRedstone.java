package rzk.wirelessredstone;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;
import rzk.wirelessredstone.registry.ModTiles;
import rzk.wirelessredstone.util.WRCreativeTab;

@Mod(modid = WirelessRedstone.MOD_ID)
public class WirelessRedstone
{
    public static final String MOD_ID = "wirelessredstone";

    public static final CreativeTabs CREATIVE_TAB = new WRCreativeTab();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(ModBlocks.class);
        MinecraftForge.EVENT_BUS.register(ModItems.class);
        MinecraftForge.EVENT_BUS.register(ModTiles.class);
    }
}
