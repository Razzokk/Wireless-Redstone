package rzk.wirelessredstone;

import com.google.common.collect.Ordering;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rzk.wirelessredstone.integration.TOPIntegration;
import rzk.wirelessredstone.packet.PacketHandler;
import rzk.wirelessredstone.proxy.ClientProxy;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;
import rzk.wirelessredstone.registry.ModTiles;

import java.util.Comparator;

@Mod(WirelessRedstone.MOD_ID)
public class WirelessRedstone
{
	public static final String MOD_ID = "wirelessredstone";
	public static final Logger LOGGER = LogManager.getLogger();

	public static Comparator<ItemStack> comparator;
	public static final ItemGroup ITEM_GROUP_WIRELESS_REDSTONE = new ItemGroup(MOD_ID)
	{
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon()
		{
			return ModBlocks.TRANSMITTER.get().asItem().getDefaultInstance();
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public void fill(NonNullList<ItemStack> items)
		{
			super.fill(items);
			items.sort(comparator);
		}
	};

	public WirelessRedstone()
	{
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		eventBus.addListener(this::setup);
		eventBus.addListener(this::imce);
		eventBus.addListener(ClientProxy::clientSetup);

		ModBlocks.BLOCKS.register(eventBus);
		ModBlocks.ITEMS.register(eventBus);
		ModItems.ITEMS.register(eventBus);
		ModTiles.TILES.register(eventBus);
	}

	private void setup(FMLCommonSetupEvent event)
	{
		PacketHandler.registerMessages();

		comparator = Comparator.comparing(ItemStack::getItem, Ordering.explicit(
				ModBlocks.TRANSMITTER.get().asItem(),
				ModBlocks.RECEIVER.get().asItem(),
				ModItems.REMOTE.get(),
				ModItems.FREQUENCY_COPIER.get(),
				ModItems.CIRCUIT.get()));
	}

	private void imce(InterModEnqueueEvent event)
	{
		if (ModList.get().isLoaded("theoneprobe"))
			TOPIntegration.register();
	}
}
