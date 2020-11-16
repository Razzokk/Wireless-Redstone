package rzk.wirelessredstone.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import rzk.lib.mc.item.IItemProvider;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.BlockFrequency;

import java.util.function.Supplier;

public class ModBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WirelessRedstone.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WirelessRedstone.MOD_ID);

	public static final RegistryObject<Block> TRANSMITTER = registerBlock("wireless_transmitter", () -> new BlockFrequency(true));
	public static final RegistryObject<Block> RECEIVER = registerBlock("wireless_receiver", () -> new BlockFrequency(false));

	public static RegistryObject<Block> registerBlock(String name, Supplier<Block> blockSupplier, IItemProvider itemProvider)
	{
		RegistryObject<Block> block = BLOCKS.register(name, blockSupplier);
		ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(WirelessRedstone.ITEM_GROUP_WIRELESS_REDSTONE)));
		return block;
	}

	public static RegistryObject<Block> registerBlock(String name, Supplier<Block> blockSupplier)
	{
		return registerBlock(name, blockSupplier, block -> new BlockItem(block, new Item.Properties().group(WirelessRedstone.ITEM_GROUP_WIRELESS_REDSTONE)));
	}

	public static RegistryObject<Block> registerBlockNoItem(String name, Supplier<Block> blockSupplier)
	{
		return BLOCKS.register(name, blockSupplier);
	}
}
