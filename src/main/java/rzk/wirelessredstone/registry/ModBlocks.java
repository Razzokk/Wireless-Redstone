package rzk.wirelessredstone.registry;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.IForgeRegistry;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.rsnetwork.Device;

import java.util.List;
import java.util.function.Function;

public final class ModBlocks
{
	private static final List<Block> BLOCKS = new ObjectArrayList<>();
	private static final List<Item> ITEMS = new ObjectArrayList<>();

	public static Block redstoneTransmitter;
	public static Block redstoneReceiver;

	private ModBlocks() {}

	private static void initBlocks()
	{
		redstoneTransmitter = registerBlock("redstone_transmitter", new BlockFrequency(Device.Type.TRANSMITTER));
		redstoneReceiver = registerBlock("redstone_receiver", new BlockFrequency(Device.Type.RECEIVER));
	}

	public static Block registerBlock(String name, Block block)
	{
		return registerBlock(name, block, blockToItem -> new ItemNameBlockItem(blockToItem, ModItems.defaultItemProperties()));
	}

	public static Block registerBlock(String name, Block block, Function<Block, Item> itemProvider)
	{
		registerBlockNoItem(name, block);
		Item item = itemProvider.apply(block);
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
		IForgeRegistry<Block> blockRegistry = event.getRegistry();
		BLOCKS.forEach(blockRegistry::register);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> itemRegistry = event.getRegistry();
		ITEMS.forEach(itemRegistry::register);
	}

	@SubscribeEvent
	public static void loadComplete(FMLLoadCompleteEvent event)
	{
		BLOCKS.clear();
		ITEMS.clear();
	}
}
