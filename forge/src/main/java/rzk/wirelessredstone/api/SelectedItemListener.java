package rzk.wirelessredstone.api;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface SelectedItemListener
{
	/**
	 * Called on server and client side.
	 *
	 * @param stack stack which was potentially active
	 * @param level the level
	 * @param user  living entity this stack belongs to
	 */
	void onClearActiveItem(ItemStack stack, Level level, LivingEntity user);
}
