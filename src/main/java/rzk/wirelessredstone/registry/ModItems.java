package rzk.wirelessredstone.registry;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.ItemFrequency;
import rzk.wirelessredstone.item.ItemRemote;
import rzk.wirelessredstone.item.ItemSniffer;

import java.util.List;

public final class ModItems
{
	private static final List<Item> ITEMS = new ObjectArrayList<>();

	public static Item circuit;
	public static Item frequencyTool;
	public static Item remote;
	public static Item sniffer;

	private ModItems() {}

	private static void initItems()
	{
		circuit = registerItem("circuit", new Item(defaultItemProperties()));
		frequencyTool = registerItem("frequency_tool", new ItemFrequency());
		remote = registerItem("remote", new ItemRemote());
		sniffer = registerItem("sniffer", new ItemSniffer());
	}

	public static Item registerItem(String name, Item item)
	{
		item.setRegistryName(WirelessRedstone.MOD_ID, name);
		ITEMS.add(item);
		return item;
	}

	public static Item.Properties defaultItemProperties()
	{
		return new Item.Properties().tab(WirelessRedstone.CREATIVE_MODE_TAB);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		initItems();
		ITEMS.forEach(event.getRegistry()::register);
		ITEMS.clear();
	}
}
