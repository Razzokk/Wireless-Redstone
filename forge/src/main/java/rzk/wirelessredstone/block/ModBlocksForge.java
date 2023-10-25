package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import rzk.wirelessredstone.WirelessRedstone;

public final class ModBlocksForge
{
	private ModBlocksForge() {}

	public static void registerBlocks(RegisterEvent event)
	{
		event.register(ForgeRegistries.Keys.BLOCKS, helper ->
		{
			ModBlocks.redstoneTransmitter = registerBlock(helper, "redstone_transmitter", new RedstoneTransmitterBlock());
			ModBlocks.redstoneReceiver = registerBlock(helper, "redstone_receiver", new RedstoneReceiverBlock());
		});
	}

	private static Block registerBlock(RegisterEvent.RegisterHelper<Block> helper, String name, Block block)
	{
		helper.register(WirelessRedstone.identifier(name), block);
		return block;
	}
}
