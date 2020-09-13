package rzk.wirelessredstone.registry;

import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import rzk.lib.mc.registry.ModRegistry;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.ItemFrequencyCopier;
import rzk.wirelessredstone.item.ItemWirelessRemote;

import java.util.ArrayList;
import java.util.List;

public class ModItems
{
	public static final List<Item> ITEMS = new ArrayList<>();

	public static final Item CIRCUIT = registerItem(new Item(new Item.Properties().group(WirelessRedstone.ITEM_GROUP_WIRELESS_REDSTONE)), "wireless_circuit");
	public static final Item REMOTE = registerItem(new ItemWirelessRemote(), "wireless_remote");
	public static final Item FREQUENCY_COPIER = registerItem(new ItemFrequencyCopier(), "frequency_copier");

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> registry = event.getRegistry();
		ITEMS.forEach(registry::register);
	}

	public static Item registerItem(Item item, String name)
	{
		return ModRegistry.registerItem(WirelessRedstone.MODID, ITEMS, item, name);
	}
}
