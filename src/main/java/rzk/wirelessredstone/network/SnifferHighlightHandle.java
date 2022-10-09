package rzk.wirelessredstone.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import rzk.wirelessredstone.item.SnifferItem;

public class SnifferHighlightHandle
{
	public static void handle(SnifferHighlightPacket packet)
	{
		Player player = Minecraft.getInstance().player;
		ItemStack stack = player.getItemInHand(packet.hand);
		if (stack.getItem() instanceof SnifferItem item)
			item.setHighlightedBlocks(packet.timestamp, stack, packet.coords);
	}
}
