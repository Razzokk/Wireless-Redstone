package rzk.wirelessredstone.registry;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import rzk.lib.mc.registry.ModRegistry;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.BlockFrequency;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks
{
	public static final List<Block> BLOCKS = new ArrayList<>();

	public static final Block TRANSMITTER = registerBlock(new BlockFrequency(true), "wireless_transmitter");
	public static final Block RECEIVER = registerBlock(new BlockFrequency(false), "wireless_receiver");

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		IForgeRegistry<Block> registry = event.getRegistry();
		BLOCKS.forEach(registry::register);
	}

	public static Block registerBlock(Block block, String name)
	{
		return ModRegistry.registerBlock(WirelessRedstone.MODID, BLOCKS, ModItems.ITEMS, block, name);
	}
}
