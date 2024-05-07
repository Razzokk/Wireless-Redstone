package rzk.wirelessredstone.registry;

import net.minecraft.registry.Registries;
import net.neoforged.neoforge.registries.RegisterEvent;
import rzk.wirelessredstone.block.P2pRedstoneReceiverBlockWrapper;
import rzk.wirelessredstone.block.P2pRedstoneTransmitterBlockWrapper;
import rzk.wirelessredstone.block.RedstoneReceiverBlockWrapper;
import rzk.wirelessredstone.block.RedstoneTransmitterBlockWrapper;
import rzk.wirelessredstone.misc.WREvents;

public final class ModBlocksNeo
{
	private ModBlocksNeo() {}

	public static void registerBlocks(RegisterEvent event)
	{
		event.register(Registries.BLOCK.getKey(), helper ->
		{
			ModBlocks.redstoneTransmitter = WREvents.register(helper, "redstone_transmitter", new RedstoneTransmitterBlockWrapper());
			ModBlocks.redstoneReceiver = WREvents.register(helper, "redstone_receiver", new RedstoneReceiverBlockWrapper());
			ModBlocks.p2pRedstoneTransmitter = WREvents.register(helper, "p2p_redstone_transmitter", new P2pRedstoneTransmitterBlockWrapper());
			ModBlocks.p2pRedstoneReceiver = WREvents.register(helper, "p2p_redstone_receiver", new P2pRedstoneReceiverBlockWrapper());
		});
	}
}
