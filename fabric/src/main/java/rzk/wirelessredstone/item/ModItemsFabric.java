package rzk.wirelessredstone.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import rzk.wirelessredstone.WirelessRedstone;

public final class ModItemsFabric
{
	private ModItemsFabric() {}

	public static void registerItems()
	{
		ModItems.circuit = registerItem("circuit", new Item(new Item.Settings()));
		ModItems.frequencyTool = registerItem("frequency_tool", new FrequencyItem(new FabricItemSettings()));
		ModItems.frequencySniffer = registerItem("frequency_sniffer", new SnifferItem(new FabricItemSettings()));
		ModItems.remote = registerItem("remote", new RemoteItem(new FabricItemSettings()));
	}

	public static Item registerItem(String name, Item item)
	{
		Registry.register(Registries.ITEM, WirelessRedstone.identifier(name), item);
		return item;
	}
}
