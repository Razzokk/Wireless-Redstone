package rzk.wirelessredstone;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;
import rzk.wirelessredstone.registry.ModTiles;
import rzk.wirelessredstone.util.WRCommands;
import rzk.wirelessredstone.util.WRItemGroup;

@Mod(WirelessRedstone.MOD_ID)
public final class WirelessRedstone
{
	public static final String MOD_ID = "wirelessredstone";
	public static final ItemGroup ITEM_GROUP = new WRItemGroup();

	public WirelessRedstone()
	{
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener(this::setup);
		eventBus.register(ModBlocks.class);
		eventBus.register(ModTiles.class);
		eventBus.register(ModItems.class);
		MinecraftForge.EVENT_BUS.register(WRCommands.class);
	}

	private void setup(FMLCommonSetupEvent event)
	{

	}
}
