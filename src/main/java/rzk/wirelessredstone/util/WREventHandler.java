package rzk.wirelessredstone.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import rzk.wirelessredstone.item.ItemRemote;

public class WREventHandler
{
	private static void powerOffRemote(World world, ItemStack stack)
	{
		if (!world.isRemote && stack.getItem() instanceof ItemRemote && ItemRemote.isPowered(stack))
			ItemRemote.setPowered(world, stack, false);
	}

	@SubscribeEvent
	public static void onPlayerToss(ItemTossEvent event)
	{
		World world = event.getPlayer().getEntityWorld();
		powerOffRemote(world, event.getEntityItem().getItem());
	}

	@SubscribeEvent
	public static void onPlayerDrops(PlayerDropsEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.getEntityWorld();
		powerOffRemote(world, player.getActiveItemStack());
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();
		powerOffRemote(world, player.getActiveItemStack());
	}
}
