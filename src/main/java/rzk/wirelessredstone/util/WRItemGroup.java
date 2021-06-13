package rzk.wirelessredstone.util;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import rzk.wirelessredstone.WirelessRedstone;

public class WRItemGroup extends ItemGroup
{
	public WRItemGroup()
	{
		super(WirelessRedstone.MOD_ID);
	}

	@Override
	public ItemStack makeIcon()
	{
		return new ItemStack(Items.REDSTONE);
	}
}
