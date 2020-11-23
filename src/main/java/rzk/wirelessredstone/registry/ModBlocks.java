package rzk.wirelessredstone.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.BlockFrequency;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks
{
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, WirelessRedstone.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, WirelessRedstone.MOD_ID);

	public static final RegistryObject<Block> TRANSMITTER = registerBlock("wireless_transmitter", () -> new BlockFrequency(true));
	public static final RegistryObject<Block> RECEIVER = registerBlock("wireless_receiver", () -> new BlockFrequency(false));

	public static RegistryObject<Block> registerBlock(String name, Supplier<Block> blockSupplier, Function<Block, BlockItem> itemProvider)
	{
		RegistryObject<Block> block = BLOCKS.register(name, blockSupplier);
		ITEMS.register(name, () -> itemProvider.apply(block.get()));
		return block;
	}

	public static RegistryObject<Block> registerBlock(String name, Supplier<Block> blockSupplier)
	{
		return registerBlock(name, blockSupplier, block -> new BlockItem(block, ModItems.defaultItemProperties()));
	}

	public static RegistryObject<Block> registerBlockNoItem(String name, Supplier<Block> blockSupplier)
	{
		return BLOCKS.register(name, blockSupplier);
	}
}
