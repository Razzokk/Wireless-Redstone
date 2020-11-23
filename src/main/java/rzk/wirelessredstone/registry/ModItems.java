package rzk.wirelessredstone.registry;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.ItemFrequencyCopier;
import rzk.wirelessredstone.item.ItemWirelessRemote;

public class ModItems
{
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, WirelessRedstone.MOD_ID);

	public static final RegistryObject<Item> CIRCUIT = ITEMS.register("wireless_circuit", () -> new Item(new Item.Properties().group(WirelessRedstone.ITEM_GROUP_WIRELESS_REDSTONE)));
	public static final RegistryObject<Item> REMOTE = ITEMS.register("wireless_remote", ItemWirelessRemote::new);
	public static final RegistryObject<Item> FREQUENCY_COPIER = ITEMS.register("frequency_copier", ItemFrequencyCopier::new);

	public static Item.Properties defaultItemProperties()
	{
		return new Item.Properties().group(WirelessRedstone.ITEM_GROUP_WIRELESS_REDSTONE);
	}
}
