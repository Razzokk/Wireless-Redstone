package rzk.wirelessredstone.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.item.LinkerItem;
import rzk.wirelessredstone.item.SnifferItem;

import java.util.function.Supplier;

public class ModItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WirelessRedstone.MODID);

	public static final RegistryObject<Item> CIRCUIT = registerItem("circuit", () -> new Item(defaultItemProps()));
	public static final RegistryObject<Item> FREQUENCY_TOOL = registerItem("frequency_tool", () -> new FrequencyItem(defaultItemProps()));
	public static final RegistryObject<Item> FREQUENCY_SNIFFER = registerItem("frequency_sniffer", () -> new SnifferItem(defaultItemProps()));

//	public static final RegistryObject<Item> P2P_LINKER = registerItem("p2p_linker", () -> new LinkerItem(defaultItemProps()));

	public static RegistryObject<Item> registerItem(String name, Supplier<Item> supplier)
	{
		return ITEMS.register(name, supplier);
	}

	public static Item.Properties defaultItemProps()
	{
		return new Item.Properties().tab(WirelessRedstone.CREATIVE_TAB);
	}
}
