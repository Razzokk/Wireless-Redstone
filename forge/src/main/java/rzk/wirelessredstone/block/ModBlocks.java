package rzk.wirelessredstone.block;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rzk.wirelessredstone.WirelessRedstone;

import java.util.function.Supplier;

public class ModBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WirelessRedstone.MODID);

	public static final RegistryObject<Block> REDSTONE_TRANSMITTER = registerBlock("redstone_transmitter", RedstoneTransmitterBlock::new);
	public static final RegistryObject<Block> REDSTONE_RECEIVER = registerBlock("redstone_receiver", RedstoneReceiverBlock::new);

	public static RegistryObject<Block> registerBlock(String name, Supplier<Block> block)
	{
		return BLOCKS.register(name, block);
	}
}
