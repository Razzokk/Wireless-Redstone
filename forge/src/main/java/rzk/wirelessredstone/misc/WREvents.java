package rzk.wirelessredstone.misc;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.item.ModItems;
import rzk.wirelessredstone.item.RemoteItem;

public class WREvents
{
	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		Player player = event.getEntity();
		Level level = player.level();
		ItemStack stack = player.getUseItem();
		if (level.isClientSide || !stack.is(ModItems.REMOTE.get())) return;
		((RemoteItem) stack.getItem()).onDeactivation(stack, level, player);
	}
}
