package rzk.wirelessredstone.registries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.blocks.ReceiverBlock;
import rzk.wirelessredstone.blocks.TransmitterBlock;
import rzk.wirelessredstone.blocks.WirelessBlock;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WirelessRedstone.MODID);

	public static final RegistryObject<Block> TRANSMITTER = registerBlock("transmitter", () -> new TransmitterBlock(BlockBehaviour.Properties.of(Material.STONE)));
	public static final RegistryObject<Block> RECEIVER = registerBlock("receiver", () -> new ReceiverBlock(BlockBehaviour.Properties.of(Material.STONE)));

	public static RegistryObject<Block> registerBlockNoItem(String name, Supplier<Block> supplier)
	{
		return BLOCKS.register(name, supplier);
	}

	public static RegistryObject<Block> registerBlock(String name, Supplier<Block> supplier, Function<RegistryObject<Block>, Supplier<Item>> itemProvider)
	{
		RegistryObject<Block> block = registerBlockNoItem(name, supplier);
		ModItems.registerItem(name, itemProvider.apply(block));
		return block;
	}

	public static RegistryObject<Block> registerBlock(String name, Supplier<Block> supplier)
	{
		return registerBlock(name, supplier, blockToItem -> () -> new BlockItem(blockToItem.get(), ModItems.defaultItemProps()));
	}
}
