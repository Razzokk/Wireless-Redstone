package rzk.wirelessredstone.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.item.ItemRemote;

public class WREventHandler
{
	private static void powerOffRemote(Level world, ItemStack stack)
	{
		if (!world.isClientSide && stack.getItem() instanceof ItemRemote && ItemRemote.isPowered(stack))
			ItemRemote.setPowered(world, stack, false);
	}

	@SubscribeEvent
	public static void onPlayerToss(ItemTossEvent event)
	{
		powerOffRemote(event.getPlayer().level, event.getEntityItem().getItem());
	}

	@SubscribeEvent
	public static void onPlayerDrops(LivingDropsEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		powerOffRemote(entity.level, entity.getUseItem());
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		Player player = event.getPlayer();
		powerOffRemote(player.level, player.getUseItem());
	}
}
