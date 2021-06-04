package rzk.wirelessredstone.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import rzk.wirelessredstone.item.ItemRemote;

public class WREventHandler
{
	@SubscribeEvent
	public static void onPlayerToss(ItemTossEvent event)
	{
		World world = event.getPlayer().getEntityWorld();

		if (!world.isRemote)
		{
			ItemStack stack = event.getEntityItem().getItem();

			if (stack.getItem() instanceof ItemRemote && ItemRemote.isPowered(stack))
				ItemRemote.setPowered(world, stack, false);
		}
	}

	@SubscribeEvent
	public static void onPlayerDrops(PlayerDropsEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.getEntityWorld();

		if (!world.isRemote)
		{
			ItemStack stack = player.getActiveItemStack();

			if (stack.getItem() instanceof ItemRemote && ItemRemote.isPowered(stack))
				ItemRemote.setPowered(world, stack, false);
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();

		if (!world.isRemote)
		{
			ItemStack stack = player.getActiveItemStack();

			if (stack.getItem() instanceof ItemRemote && ItemRemote.isPowered(stack))
				ItemRemote.setPowered(world, stack, false);
		}
	}
}
