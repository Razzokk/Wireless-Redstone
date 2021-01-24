package rzk.wirelessredstone.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.registry.ModBlocks;

public class WRCreativeTab extends CreativeTabs
{
    public WRCreativeTab()
    {
        super(WirelessRedstone.MOD_ID);
    }

    @Override
    public ItemStack getTabIconItem()
    {
        return new ItemStack(ModBlocks.redstoneTransmitter);
    }
}
