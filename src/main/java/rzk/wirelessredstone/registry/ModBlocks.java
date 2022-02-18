package rzk.wirelessredstone.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.rsnetwork.Device;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class ModBlocks
{
	private static final List<Block> BLOCK_CACHE = new ArrayList<>();
	private static final List<Item> ITEM_CACHE = new ArrayList<>();

	private ModBlocks() {}

	public static Block redstoneTransmitter;
	public static Block redstoneReceiver;

	private static void initBlocks()
	{
		redstoneTransmitter = registerBlock("redstone_transmitter", new BlockFrequency(Device.Type.TRANSMITTER));
		redstoneReceiver = registerBlock("redstone_receiver", new BlockFrequency(Device.Type.RECEIVER));
	}

	public static Block registerBlockNoItem(String name, Block block)
	{
		block.setRegistryName(WirelessRedstone.MOD_ID, name);
		BLOCK_CACHE.add(block);
		return block;
	}

	public static Block registerBlock(String name, Block block, Function<Block, Item> itemProvider)
	{
		registerBlockNoItem(name, block);
		Item item = itemProvider.apply(block);
		item.setRegistryName(block.getRegistryName());
		ITEM_CACHE.add(item);
		return block;
	}

	public static Block registerBlock(String name, Block block)
	{
		return registerBlock(name, block, blockToItem -> new BlockItem(blockToItem, ModItems.defaultItemProps()));
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		initBlocks();
		IForgeRegistry<Block> registry = event.getRegistry();
		BLOCK_CACHE.forEach(registry::register);
		BLOCK_CACHE.clear();
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		IForgeRegistry<Item> registry = event.getRegistry();
		ITEM_CACHE.forEach(registry::register);
		ITEM_CACHE.clear();
	}
}
