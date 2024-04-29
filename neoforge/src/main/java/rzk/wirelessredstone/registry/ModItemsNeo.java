package rzk.wirelessredstone.registry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.neoforged.neoforge.registries.RegisterEvent;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.item.RemoteItemWrapper;
import rzk.wirelessredstone.item.SnifferItem;
import rzk.wirelessredstone.misc.WREvents;

public final class ModItemsNeo
{
	private ModItemsNeo() {}

	public static void registerItems(RegisterEvent event)
	{
		event.register(Registries.ITEM.getKey(), helper ->
		{
			WREvents.register(helper, "redstone_transmitter", new BlockItem(ModBlocks.redstoneTransmitter, new Item.Settings()));
			WREvents.register(helper, "redstone_receiver", new BlockItem(ModBlocks.redstoneReceiver, new Item.Settings()));

			ModItems.circuit = WREvents.register(helper, "circuit", new Item(new Item.Settings()));
			ModItems.frequencyTool = WREvents.register(helper, "frequency_tool", new FrequencyItem(new Item.Settings()));
			ModItems.frequencySniffer = WREvents.register(helper, "frequency_sniffer", new SnifferItem(new Item.Settings()));
			ModItems.remote = WREvents.register(helper, "remote", new RemoteItemWrapper(new Item.Settings()));
		});
	}
}
