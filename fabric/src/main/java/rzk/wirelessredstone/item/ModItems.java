package rzk.wirelessredstone.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import rzk.wirelessredstone.WirelessRedstone;

public class ModItems
{
	public static final Item CIRCUIT = new Item(defaultSettings());
	public static final Item FREQUENCY_TOOL = new FrequencyItem(defaultSettings());
	public static final Item FREQUENCY_SNIFFER = new SnifferItem(defaultSettings());
	public static final Item REMOTE = new RemoteItem(defaultSettings());

	public static void registerItems()
	{
		registerItem("circuit", CIRCUIT);
		registerItem("frequency_tool", FREQUENCY_TOOL);
		registerItem("frequency_sniffer", FREQUENCY_SNIFFER);
		registerItem("remote", REMOTE);
	}

	public static void registerItem(String name, Item item)
	{
		Registry.register(Registry.ITEM, WirelessRedstone.identifier(name), item);
	}

	public static Item.Settings defaultSettings()
	{
		return new FabricItemSettings().group(WirelessRedstone.ITEM_GROUP);
	}
}
