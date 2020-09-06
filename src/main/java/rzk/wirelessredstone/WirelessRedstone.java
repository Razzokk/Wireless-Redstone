package rzk.wirelessredstone;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rzk.wirelessredstone.packet.PacketHandler;
import rzk.wirelessredstone.proxy.ClientProxy;
import rzk.wirelessredstone.proxy.IProxy;
import rzk.wirelessredstone.proxy.ServerProxy;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;
import rzk.wirelessredstone.registry.ModTiles;

@Mod(WirelessRedstone.MODID)
public class WirelessRedstone
{
	public static final String MODID = "wirelessredstone";
	public static final Logger LOGGER = LogManager.getLogger();

	public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

	public static final ItemGroup ITEM_GROUP_WIRELESS_REDSTONE = new ItemGroup(MODID)
	{
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon()
		{
			return Items.REDSTONE.getDefaultInstance();
		}
	};

	public WirelessRedstone()
	{
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		eventBus.addListener(this::setup);
		eventBus.addListener(proxy::clientSetup);
		eventBus.register(ModBlocks.class);
		eventBus.register(ModItems.class);
		eventBus.register(ModTiles.class);
	}

	private void setup(FMLCommonSetupEvent event)
	{
		PacketHandler.registerMessages();
	}
}
