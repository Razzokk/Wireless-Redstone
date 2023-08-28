package rzk.wirelessredstone;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import rzk.wirelessredstone.block.ModBlocks;
import rzk.wirelessredstone.block.entity.ModBlockEntities;
import rzk.wirelessredstone.client.WirelessRedstoneClient;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.item.ModItems;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.misc.WREvents;
import rzk.wirelessredstone.network.ModNetworking;

@Mod(WirelessRedstone.MODID)
public class WirelessRedstone
{
	public static final String MODID = "wirelessredstone";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final CreativeModeTab CREATIVE_MODE_TAB = new CreativeModeTab(identifier(MODID).toLanguageKey()) {
		@Override
		public ItemStack makeIcon()
		{
			return new ItemStack(ModBlocks.REDSTONE_TRANSMITTER.get());
		}
	};

	public WirelessRedstone()
	{
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::loadComplete);
		modEventBus.addListener(WirelessRedstoneClient::clientSetup);
		modEventBus.addListener(WirelessRedstoneClient::onRegisterRenderers);

		ModBlocks.BLOCKS.register(modEventBus);
		ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);

		MinecraftForge.EVENT_BUS.register(WREvents.class);
	}

	public static ResourceLocation identifier(String path)
	{
		return new ResourceLocation(MODID, path);
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		ModNetworking.registerMessages();
	}

	private void loadComplete(FMLLoadCompleteEvent event)
	{
		WRConfig.load();

		if (ModList.get().isLoaded("cloth_config"))
			ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
				() -> new ConfigScreenHandler.ConfigScreenFactory(ModScreens::getConfigScreen));
	}
}
