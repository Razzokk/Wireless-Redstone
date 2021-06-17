package rzk.wirelessredstone.util;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.registry.ModBlocks;

public class WRItemGroup extends ItemGroup
{
	public WRItemGroup()
	{
		super(WirelessRedstone.MOD_ID);
	}

	@Override
	public ItemStack makeIcon()
	{
		return new ItemStack(ModBlocks.redstoneTransmitter);
	}
}
