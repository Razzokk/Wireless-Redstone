package rzk.wirelessredstone.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import rzk.wirelessredstone.WirelessRedstone;

public class ModItems
{
	public static final Item CIRCUIT = new Item(new Item.Settings());
	public static final Item FREQUENCY_TOOL = new FrequencyItem(new Item.Settings());
	public static final Item FREQUENCY_SNIFFER = new SnifferItem(new Item.Settings());
	public static final Item REMOTE = new RemoteItem(new Item.Settings());

	public static void registerItems()
	{
		registerItem("circuit", CIRCUIT);
		registerItem("frequency_tool", FREQUENCY_TOOL);
		registerItem("frequency_sniffer", FREQUENCY_SNIFFER);
		registerItem("remote", REMOTE);
	}

	public static void registerItem(String name, Item item)
	{
		Registry.register(Registries.ITEM, WirelessRedstone.identifier(name), item);
	}
}
