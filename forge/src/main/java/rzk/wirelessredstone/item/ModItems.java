package rzk.wirelessredstone.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.ModBlocks;

import java.util.function.Supplier;

public class ModItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WirelessRedstone.MODID);

	public static final RegistryObject<Item> REDSTONE_TRANSMITTER = registerItem("redstone_transmitter", () -> new BlockItem(ModBlocks.REDSTONE_TRANSMITTER.get(), defaultProperties()));
	public static final RegistryObject<Item> REDSTONE_RECEIVER = registerItem("redstone_receiver", () -> new BlockItem(ModBlocks.REDSTONE_RECEIVER.get(), defaultProperties()));

	public static final RegistryObject<Item> CIRCUIT = registerItem("circuit", () -> new Item(defaultProperties()));
	public static final RegistryObject<Item> FREQUENCY_TOOL = registerItem("frequency_tool", () -> new FrequencyItem(defaultProperties()));
	public static final RegistryObject<Item> FREQUENCY_SNIFFER = registerItem("frequency_sniffer", () -> new SnifferItem(defaultProperties()));
	public static final RegistryObject<Item> REMOTE = registerItem("remote", () -> new RemoteItem(defaultProperties()));

	public static RegistryObject<Item> registerItem(String name, Supplier<Item> item)
	{
		return ITEMS.register(name, item);
	}

	public static Item.Properties defaultProperties()
	{
		return new Item.Properties().tab(WirelessRedstone.CREATIVE_MODE_TAB);
	}
}
