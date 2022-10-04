package rzk.wirelessredstone.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import rzk.wirelessredstone.misc.Utils;

public class FrequencyItem extends Item
{
	public FrequencyItem(Properties props)
	{
		super(props);
	}

	public void setFrequency(ItemStack stack, int frequency)
	{
		Utils.writeFrequency(stack.getOrCreateTag(), frequency);
	}
}
