package rzk.wirelessredstone;

import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import rzk.wirelessredstone.block.ModBlocks;
import rzk.wirelessredstone.block.ModBlocksForge;
import rzk.wirelessredstone.block.entity.ModBlockEntitiesForge;
import rzk.wirelessredstone.client.WirelessRedstoneClientForge;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.item.ModItems;
import rzk.wirelessredstone.item.ModItemsForge;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.misc.WREvents;
import rzk.wirelessredstone.network.ModNetworking;

@Mod(WirelessRedstoneForge.MODID)
public class WirelessRedstoneForge
{
	public static final String MODID = "wirelessredstone";

	public WirelessRedstoneForge()
	{
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::loadComplete);
		modEventBus.addListener(this::onCreativeTabEvent);
		modEventBus.addListener(WirelessRedstoneClientForge::clientSetup);
		modEventBus.addListener(WirelessRedstoneClientForge::onRegisterRenderers);

		modEventBus.addListener(ModBlocksForge::registerBlocks);
		modEventBus.addListener(ModBlockEntitiesForge::registerBlockEntities);
		modEventBus.addListener(ModItemsForge::registerItems);

		MinecraftForge.EVENT_BUS.register(WREvents.class);
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

	private void onCreativeTabEvent(RegisterEvent event)
	{
		event.register(RegistryKeys.ITEM_GROUP, helper ->
			helper.register(WirelessRedstone.identifier("item_group"), ItemGroup.builder()
				.displayName(Text.translatable(TranslationKeys.ITEM_GROUP_WIRELESS_REDSTONE))
				.icon(() -> ModBlocks.redstoneTransmitter.asItem().getDefaultStack())
				.entries((params, output) ->
				{
					output.add(ModBlocks.redstoneTransmitter);
					output.add(ModBlocks.redstoneReceiver);
					output.add(ModItems.circuit);
					output.add(ModItems.frequencyTool);
					output.add(ModItems.frequencySniffer);
					output.add(ModItems.remote);
				})
				.build()));
	}
}
