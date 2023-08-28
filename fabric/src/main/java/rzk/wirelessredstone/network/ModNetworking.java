package rzk.wirelessredstone.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.item.ModItems;
import rzk.wirelessredstone.item.RemoteItem;
import rzk.wirelessredstone.network.packet.Packet;
import rzk.wirelessredstone.network.packet.PacketType;
import rzk.wirelessredstone.network.packet.ServerPacketHandler;

public class ModNetworking
{
	public static void register()
	{
		registerGlobalServerReceiver(FrequencyBlockPacket.TYPE, (packet, player, responseSender) ->
		{
			World world = player.getWorld();
			if (world.getBlockState(packet.pos).getBlock() instanceof RedstoneTransceiverBlock block)
				block.setFrequency(world, packet.pos, packet.frequency);
		});

		registerGlobalServerReceiver(FrequencyItemPacket.TYPE, (packet, player, responseSender) ->
		{
			ItemStack stack = player.getStackInHand(packet.hand);
			if (stack.getItem() instanceof FrequencyItem item)
				item.setFrequency(stack, packet.frequency);
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
		{
			ServerPlayerEntity player = handler.player;
			ItemStack stack = player.getActiveItem();
			if (!stack.isOf(ModItems.REMOTE)) return;
			((RemoteItem) stack.getItem()).onDeactivation(stack, player.getWorld(), player);
		});
	}

	private static <T extends Packet> void registerGlobalServerReceiver(PacketType<T> packetType, ServerPacketHandler<T> handler)
	{
		ServerPlayNetworking.registerGlobalReceiver(packetType.identifier, (server, player, ignore, buf, responseSender) ->
		{
			T packet = packetType.createPacket(buf);
			server.execute(() -> handler.handle(packet, player, responseSender));
		});
	}

	public static <T extends Packet> void send(ServerPlayerEntity player, T packet)
	{
		PacketByteBuf buffer = PacketByteBufs.create();
		packet.write(buffer);
		ServerPlayNetworking.send(player, packet.getType().identifier, buffer);
	}
}
