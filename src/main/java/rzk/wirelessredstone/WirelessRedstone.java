package rzk.wirelessredstone;

import com.google.common.collect.Ordering;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
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

import java.util.Collections;
import java.util.Comparator;

@Mod(WirelessRedstone.MODID)
public class WirelessRedstone
{
	public static final String MODID = "wirelessredstone";
	public static final Logger LOGGER = LogManager.getLogger();

	public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

	public static Comparator<ItemStack> comparator;
	public static final ItemGroup ITEM_GROUP_WIRELESS_REDSTONE = new ItemGroup(MODID)
	{
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon()
		{
			return ModBlocks.TRANSMITTER.asItem().getDefaultInstance();
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
		eventBus.addListener(proxy::clientSetup);
		eventBus.register(ModBlocks.class);
		eventBus.register(ModItems.class);
		eventBus.register(ModTiles.class);
	}

	private void setup(FMLCommonSetupEvent event)
	{
		PacketHandler.registerMessages();
		comparator = Ordering.explicit(
				ModBlocks.TRANSMITTER.asItem(),
				ModBlocks.RECEIVER.asItem(),
				ModItems.REMOTE,
				ModItems.FREQUENCY_COPIER,
				ModItems.CIRCUIT)
				.onResultOf(ItemStack::getItem);
	}
}
