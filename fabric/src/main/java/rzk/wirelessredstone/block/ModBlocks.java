package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.registry.Registry;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.ModItems;

import static rzk.wirelessredstone.item.ModItems.defaultSettings;

public class ModBlocks
{
	public static final Block REDSTONE_TRANSMITTER = new RedstoneTransmitterBlock();
	public static final Block REDSTONE_RECEIVER = new RedstoneReceiverBlock();

	public static void registerBlocks()
	{
		registerBlock("redstone_transmitter", REDSTONE_TRANSMITTER);
		registerBlock("redstone_receiver", REDSTONE_RECEIVER);
	}

	public static void registerBlockWithoutItem(String name, Block block)
	{
		Registry.register(Registry.BLOCK, WirelessRedstone.identifier(name), block);
	}

	public static void registerBlock(String name, Block block)
	{
		registerBlockWithoutItem(name, block);
		ModItems.registerItem(name, new BlockItem(block, defaultSettings()));
	}
}
