package rzk.wirelessredstone.registry;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import rzk.wirelessredstone.WirelessRedstone;

public final class ModItems
{
    public static final ObjectList<Item> ITEMS = new ObjectArrayList<>();

    public static Item test;

    private ModItems() {}

    private static void initItems()
    {
        test = registerItem("test_item", new Item());
    }

    private static Item registerItem(String name, Item item)
    {
        item.setCreativeTab(WirelessRedstone.CREATIVE_TAB);
        item.setUnlocalizedName(WirelessRedstone.MOD_ID + '.' + name);
        item.setRegistryName(WirelessRedstone.MOD_ID, name);
        ITEMS.add(item);
        return item;
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();
        initItems();
        ITEMS.forEach(registry::register);
    }
}
