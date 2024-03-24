package rzk.wirelessredstone.block;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
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
		});
	}
}
