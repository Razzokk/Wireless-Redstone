package rzk.wirelessredstone.registry;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.item.RemoteItem;
import rzk.wirelessredstone.item.SnifferItem;

public final class ModItemsFabric
{
	private ModItemsFabric() {}

	public static void registerItems()
	{
		ModItems.circuit = registerItem("circuit", new Item(new Item.Settings()));
		ModItems.frequencyTool = registerItem("frequency_tool", new FrequencyItem(new Item.Settings()));
		ModItems.frequencySniffer = registerItem("frequency_sniffer", new SnifferItem(new Item.Settings()));
		ModItems.remote = registerItem("remote", new RemoteItem(new Item.Settings()));
	}

	public static Item registerItem(String name, Item item)
	{
		Registry.register(Registries.ITEM, WirelessRedstone.identifier(name), item);
		return item;
	}
}
