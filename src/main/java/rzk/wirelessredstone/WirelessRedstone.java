package rzk.wirelessredstone;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import rzk.wirelessredstone.client.ClientSubscriber;
import rzk.wirelessredstone.client.screen.Screens;
import rzk.wirelessredstone.misc.Config;
import rzk.wirelessredstone.network.PacketHandler;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

@Mod(WirelessRedstone.MODID)
public class WirelessRedstone
{
	public static final String MODID = "wirelessredstone";
	public static final Logger LOGGER = LogUtils.getLogger();

	public WirelessRedstone()
	{
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::loadComplete);
		modEventBus.addListener(this::buildContents);
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
	}

	private void loadComplete(FMLLoadCompleteEvent event)
	{
		Config.updateInternals();
	}

	private void buildContents(CreativeModeTabEvent.Register event)
	{
		event.registerCreativeModeTab(new ResourceLocation(MODID, MODID), builder ->
				builder.title(Component.translatable("item_group." + MODID))
						.icon(() -> new ItemStack(ModBlocks.REDSTONE_TRANSMITTER.get()))
						.displayItems((enabledFlags, populator, hasPermissions) ->
						{
							for (RegistryObject<Block> block : ModBlocks.BLOCKS.getEntries())
								populator.accept(block.get());
							for (RegistryObject<Item> item : ModItems.ITEMS.getEntries())
								populator.accept(item.get());
						})
		);
	}
}
