package rzk.wirelessredstone.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.P2pRedstoneReceiverBlock;
import rzk.wirelessredstone.block.P2pRedstoneTransmitterBlock;
import rzk.wirelessredstone.block.RedstoneReceiverBlock;
import rzk.wirelessredstone.block.RedstoneTransmitterBlock;

public final class ModBlocksFabric
{
	private ModBlocksFabric() {}

	public static void registerBlocks()
	{
		ModBlocks.redstoneTransmitter = registerBlock("redstone_transmitter", new RedstoneTransmitterBlock());
		ModBlocks.redstoneReceiver = registerBlock("redstone_receiver", new RedstoneReceiverBlock());
		ModBlocks.p2pRedstoneTransmitter = registerBlock("p2p_redstone_transmitter", new P2pRedstoneTransmitterBlock());
		ModBlocks.p2pRedstoneReceiver = registerBlock("p2p_redstone_receiver", new P2pRedstoneReceiverBlock());
	}

	public static Block registerBlockWithoutItem(String name, Block block)
	{
		Registry.register(Registries.BLOCK, WirelessRedstone.identifier(name), block);
		return block;
	}

	public static Block registerBlock(String name, Block block)
	{
		registerBlockWithoutItem(name, block);
		ModItemsFabric.registerItem(name, new BlockItem(block, new Item.Settings()));
		return block;
	}
}
