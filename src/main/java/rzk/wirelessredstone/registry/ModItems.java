package rzk.wirelessredstone.registry;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.ItemFrequency;
import rzk.wirelessredstone.item.ItemRemote;
import rzk.wirelessredstone.item.ItemSniffer;

import java.util.ArrayList;
import java.util.List;

public final class ModItems
{
	private static final List<Item> ITEM_CACHE = new ArrayList<>();

	public static Item circuit;
	public static Item frequencyTool;
	public static Item remote;
	public static Item sniffer;

	private ModItems() {}

	private static void initItems()
	{
		circuit = registerItem("circuit", new Item(defaultItemProps()));
		frequencyTool = registerItem("frequency_tool", new ItemFrequency());
		remote = registerItem("remote", new ItemRemote());
		sniffer = registerItem("sniffer", new ItemSniffer());
	}

	public static Item registerItem(String name, Item item)
	{
		item.setRegistryName(WirelessRedstone.MOD_ID, name);
		ITEM_CACHE.add(item);
		return item;
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		initItems();
		IForgeRegistry<Item> registry = event.getRegistry();
		ITEM_CACHE.forEach(registry::register);
		ITEM_CACHE.clear();
	}

	public static Item.Properties defaultItemProps()
	{
		return new Item.Properties().tab(WirelessRedstone.ITEM_GROUP);
	}
}
