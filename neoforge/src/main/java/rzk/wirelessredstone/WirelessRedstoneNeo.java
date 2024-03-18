package rzk.wirelessredstone;

import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.RegisterEvent;
import rzk.wirelessredstone.block.ModBlocks;
import rzk.wirelessredstone.block.ModBlocksNeo;
import rzk.wirelessredstone.block.entity.ModBlockEntitiesNeo;
import rzk.wirelessredstone.client.WirelessRedstoneClientNeo;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.item.ModItems;
import rzk.wirelessredstone.item.ModItemsNeo;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.misc.WREvents;
import rzk.wirelessredstone.network.ModNetworking;

@Mod(WirelessRedstone.MODID)
public class WirelessRedstoneNeo
{
	public WirelessRedstoneNeo(IEventBus modEventBus)
	{
		modEventBus.addListener(this::loadComplete);
		modEventBus.addListener(this::onCreativeTabEvent);
		modEventBus.addListener(WirelessRedstoneClientNeo::clientSetup);
		modEventBus.addListener(WirelessRedstoneClientNeo::onRegisterRenderers);

		modEventBus.addListener(ModBlocksNeo::registerBlocks);
		modEventBus.addListener(ModBlockEntitiesNeo::registerBlockEntities);
		modEventBus.addListener(ModItemsNeo::registerItems);

		NeoForge.EVENT_BUS.register(WREvents.class);
		modEventBus.register(ModNetworking.class);
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
