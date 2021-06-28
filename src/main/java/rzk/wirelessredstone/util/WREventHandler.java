package rzk.wirelessredstone.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.item.ItemRemote;

public class WREventHandler
{
	private static void powerOffRemote(World world, ItemStack stack)
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
		PlayerEntity player = event.getPlayer();
		powerOffRemote(player.level, player.getUseItem());
	}
}
