package rzk.wirelessredstone;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import rzk.wirelessredstone.client.ClientSubscriber;
import rzk.wirelessredstone.misc.CreativeTabWR;
import rzk.wirelessredstone.network.PacketHandler;
import rzk.wirelessredstone.registries.ModBlockEntities;
import rzk.wirelessredstone.registries.ModBlocks;
import rzk.wirelessredstone.registries.ModItems;

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
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		PacketHandler.registerMessages();
	}
}
