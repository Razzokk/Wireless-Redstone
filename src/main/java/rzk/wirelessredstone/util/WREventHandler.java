package rzk.wirelessredstone.util;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rzk.wirelessredstone.item.ItemRemote;

public class WREventHandler
{
	@SubscribeEvent
	public static void onPlayerToss(ItemTossEvent event)
	{
		World world = event.getPlayer().getEntityWorld();
		ItemStack stack = event.getEntityItem().getItem();

		if (!world.isRemote && stack.getItem() instanceof ItemRemote)
			ItemRemote.setPowered(world, stack, false);
	}

	@SubscribeEvent
	public static void onRenderWorldLast(RenderWorldLastEvent event)
	{

	}
}
