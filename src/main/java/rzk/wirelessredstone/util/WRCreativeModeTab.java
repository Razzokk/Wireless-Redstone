package rzk.wirelessredstone.util;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.registry.ModBlocks;

public class WRCreativeModeTab extends CreativeModeTab
{
	public WRCreativeModeTab()
	{
		super(WirelessRedstone.MOD_ID);
	}

	@Override
	public ItemStack makeIcon()
	{
		return new ItemStack(ModBlocks.redstoneTransmitter);
	}
}
