package rzk.wirelessredstone.misc;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.registries.ModBlocks;

public class CreativeTabWR extends CreativeModeTab
{
	public CreativeTabWR()
	{
		super(WirelessRedstone.MODID);
	}

	@Override
	public ItemStack makeIcon()
	{
		return new ItemStack(ModBlocks.REDSTONE_TRANSMITTER.get());
	}
}
