package rzk.wirelessredstone.registry;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import rzk.wirelessredstone.block.P2pRedstoneReceiverBlockWrapper;
import rzk.wirelessredstone.block.P2pRedstoneTransmitterBlockWrapper;
import rzk.wirelessredstone.block.RedstoneReceiverBlockWrapper;
import rzk.wirelessredstone.block.RedstoneTransmitterBlockWrapper;
import rzk.wirelessredstone.misc.WREvents;

public final class ModBlocksForge
{
	private ModBlocksForge() {}

	public static void registerBlocks(RegisterEvent event)
	{
		event.register(ForgeRegistries.Keys.BLOCKS, helper ->
		{
			ModBlocks.redstoneTransmitter = WREvents.register(helper, "redstone_transmitter", new RedstoneTransmitterBlockWrapper());
			ModBlocks.redstoneReceiver = WREvents.register(helper, "redstone_receiver", new RedstoneReceiverBlockWrapper());
			ModBlocks.p2pRedstoneTransmitter = WREvents.register(helper, "p2p_redstone_transmitter", new P2pRedstoneTransmitterBlockWrapper());
			ModBlocks.p2pRedstoneReceiver = WREvents.register(helper, "p2p_redstone_receiver", new P2pRedstoneReceiverBlockWrapper());
		});
	}
}
