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

	public static final RegistryObject<Item> REDSTONE_TRANSMITTER = registerItem("redstone_transmitter", () -> new BlockItem(ModBlocks.REDSTONE_TRANSMITTER.get(), new Item.Properties()));
	public static final RegistryObject<Item> REDSTONE_RECEIVER = registerItem("redstone_receiver", () -> new BlockItem(ModBlocks.REDSTONE_RECEIVER.get(), new Item.Properties()));

	public static final RegistryObject<Item> CIRCUIT = registerItem("circuit", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> FREQUENCY_TOOL = registerItem("frequency_tool", () -> new FrequencyItem(new Item.Properties()));
	public static final RegistryObject<Item> FREQUENCY_SNIFFER = registerItem("frequency_sniffer", () -> new SnifferItem(new Item.Properties()));
	public static final RegistryObject<Item> REMOTE = registerItem("remote", () -> new RemoteItem(new Item.Properties()));

	public static RegistryObject<Item> registerItem(String name, Supplier<Item> item)
	{
		return ITEMS.register(name, item);
	}
}
