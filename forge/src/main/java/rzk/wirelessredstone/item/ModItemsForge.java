package rzk.wirelessredstone.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.ModBlocks;

public final class ModItemsForge
{
	private ModItemsForge() {}

	public static void registerItems(RegisterEvent event)
	{
		event.register(ForgeRegistries.Keys.ITEMS, helper ->
		{
			registerItem(helper, "redstone_transmitter", new BlockItem(ModBlocks.redstoneTransmitter, new Item.Settings()));
			registerItem(helper, "redstone_receiver", new BlockItem(ModBlocks.redstoneReceiver, new Item.Settings()));

			ModItems.circuit = registerItem(helper, "circuit", new Item(new Item.Settings()));
			ModItems.frequencyTool = registerItem(helper, "frequency_tool", new FrequencyItem(new Item.Settings()));
			ModItems.frequencySniffer = registerItem(helper, "frequency_sniffer", new SnifferItem(new Item.Settings()));
			ModItems.remote = registerItem(helper, "remote", new RemoteItemWrapper(new Item.Settings()));
		});
	}

	private static Item registerItem(RegisterEvent.RegisterHelper<Item> helper, String name, Item item)
	{
		helper.register(WirelessRedstone.identifier(name), item);
		return item;
	}
}
