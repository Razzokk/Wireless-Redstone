package rzk.wirelessredstone.registry;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.WirelessRedstone;

public final class ModItems
{
	public static final ObjectList<Item> ITEMS = new ObjectArrayList<>();

	private ModItems() {}

	private static void initItems()
	{

	}

	public static Item registerItem(String name, Item item)
	{
		item.setRegistryName(WirelessRedstone.MOD_ID, name);
		ITEMS.add(item);
		return item;
	}

	public static Item.Properties defaultItemProperties()
	{
		return new Item.Properties().tab(WirelessRedstone.ITEM_GROUP);
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		initItems();
		ITEMS.forEach(event.getRegistry()::register);
	}
}
