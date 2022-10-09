package rzk.wirelessredstone;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import rzk.wirelessredstone.client.ClientSubscriber;
import rzk.wirelessredstone.client.screen.Screens;
import rzk.wirelessredstone.misc.Config;
import rzk.wirelessredstone.misc.CreativeTabWR;
import rzk.wirelessredstone.network.PacketHandler;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

@Mod(WirelessRedstone.MODID)
public class WirelessRedstone
{
	public static final String MODID = "wirelessredstone";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final CreativeModeTab CREATIVE_TAB = new CreativeTabWR();

	public WirelessRedstone()
	{
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(ClientSubscriber::clientSetup);
		modEventBus.addListener(ClientSubscriber::onRegisterRenderers);

		ModBlocks.BLOCKS.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);
		ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);

		MinecraftForge.EVENT_BUS.register(this);

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory(Screens::openConfigScreen));
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		PacketHandler.registerMessages();
		Config.updateInternals();
	}
}
