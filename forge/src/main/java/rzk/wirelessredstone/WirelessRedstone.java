package rzk.wirelessredstone;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

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

		LOGGER.info("WirelessRedstone constructed");
	}

	private void commonSetup(FMLCommonSetupEvent event)
	{
		LOGGER.info("WirelessRedstone::commonSetup");
	}

	private void loadComplete(FMLLoadCompleteEvent event)
	{
		LOGGER.info("WirelessRedstone::loadComplete");
	}
}
