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
import rzk.wirelessredstone.blocks.ModelTest;
import rzk.wirelessredstone.blocks.P2PRedstoneReceiverBlock;
import rzk.wirelessredstone.blocks.P2PRedstoneTransmitterBlock;
import rzk.wirelessredstone.blocks.RedstoneReceiverBlock;
import rzk.wirelessredstone.blocks.RedstoneTransmitterBlock;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WirelessRedstone.MODID);

	public static final RegistryObject<Block> REDSTONE_TRANSMITTER = registerBlock("redstone_transmitter", () -> new RedstoneTransmitterBlock(BlockBehaviour.Properties.of(Material.METAL)));
	public static final RegistryObject<Block> REDSTONE_RECEIVER = registerBlock("redstone_receiver", () -> new RedstoneReceiverBlock(BlockBehaviour.Properties.of(Material.METAL)));

	public static final RegistryObject<Block> P2P_REDSTONE_TRANSMITTER = registerBlock("p2p_redstone_transmitter", () -> new P2PRedstoneTransmitterBlock(BlockBehaviour.Properties.of(Material.METAL)));
	public static final RegistryObject<Block> P2P_REDSTONE_RECEIVER = registerBlock("p2p_redstone_receiver", () -> new P2PRedstoneReceiverBlock(BlockBehaviour.Properties.of(Material.METAL)));

	public static final RegistryObject<Block> MODEL_TEST = registerBlock("model_test", () -> new ModelTest(BlockBehaviour.Properties.of(Material.METAL)));

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
