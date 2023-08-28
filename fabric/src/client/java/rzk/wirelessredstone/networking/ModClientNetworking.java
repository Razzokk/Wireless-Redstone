package rzk.wirelessredstone.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import rzk.wirelessredstone.item.SnifferItem;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.SnifferHighlightPacket;
import rzk.wirelessredstone.network.packet.Packet;
import rzk.wirelessredstone.network.packet.PacketType;
import rzk.wirelessredstone.screen.FrequencyBlockScreen;
import rzk.wirelessredstone.screen.FrequencyItemScreen;

public class ModClientNetworking
{
	public static void register()
	{
		registerGlobalClientReceiver(SnifferHighlightPacket.TYPE, (packet, player, responseSender) ->
		{
			ItemStack stack = player.getStackInHand(packet.hand);
			if (stack.getItem() instanceof SnifferItem item)
				item.setHighlightedBlocks(packet.timestamp, stack, packet.coords);
		});

		registerGlobalClientReceiver(FrequencyBlockPacket.TYPE, (packet, player, responseSender) ->
			MinecraftClient.getInstance().setScreen(new FrequencyBlockScreen(packet.frequency, packet.pos)));

		registerGlobalClientReceiver(FrequencyItemPacket.TYPE, (packet, player, responseSender) ->
			MinecraftClient.getInstance().setScreen(new FrequencyItemScreen(packet.frequency, packet.hand)));
	}

	private static <T extends Packet> void registerGlobalClientReceiver(PacketType<T> packetType, ClientPacketHandler<T> handler)
	{
		ClientPlayNetworking.registerGlobalReceiver(packetType.identifier, (client, handler1, buf, responseSender) ->
		{
			T packet = packetType.createPacket(buf);
			client.execute(() -> handler.handle(packet, client.player, responseSender));
		});
	}

	public static <T extends Packet> void send(T packet)
	{
		PacketByteBuf buffer = PacketByteBufs.create();
		packet.write(buffer);
		ClientPlayNetworking.send(packet.getType().identifier, buffer);
	}
}
