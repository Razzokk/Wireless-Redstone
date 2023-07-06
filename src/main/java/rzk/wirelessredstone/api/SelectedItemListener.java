package rzk.wirelessredstone.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public interface SelectedItemListener
{
	/**
	 * Called only on server side.
	 *
	 * @param stack stack which got dropped
	 * @param world world
	 * @param player player who held the stack
	 */
	void onSelectedItemDropped(ItemStack stack, ServerWorld world, ServerPlayerEntity player);

	/**
	 * Called on server and client side.
	 *
	 * @param stack stack which was potentially active
	 * @param world the world
	 * @param user living entity this stack belongs to
	 */
	void onClearActiveItem(ItemStack stack, World world, LivingEntity user);
}
