package rzk.wirelessredstone.registry;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.BlockBit;

import java.util.function.Function;

public final class ModBlocks
{
	public static final ObjectList<Block> BLOCKS = new ObjectArrayList<>();
	public static final ObjectList<Item> ITEMS = new ObjectArrayList<>();

	public static Block testBit;

	private ModBlocks() {}

	private static void initBlocks()
	{
		testBit = registerBlock("test_bit", new BlockBit());
	}

	public static Block registerBlock(String name, Block block)
	{
		return registerBlock(name, block, blockToItem -> new BlockNamedItem(blockToItem, ModItems.defaultItemProperties()));
	}

	public static Block registerBlock(String name, Block block, Function<Block, BlockItem> itemProvider)
	{
		registerBlockNoItem(name, block);
		BlockItem item = itemProvider.apply(block);
		item.setRegistryName(block.getRegistryName());
		ITEMS.add(item);
		return block;
	}

	public static Block registerBlockNoItem(String name, Block block)
	{
		block.setRegistryName(WirelessRedstone.MOD_ID, name);
		BLOCKS.add(block);
		return block;
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		initBlocks();
		BLOCKS.forEach(event.getRegistry()::register);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		ITEMS.forEach(event.getRegistry()::register);
	}
}
