package rzk.wirelessredstone.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.server.network.ServerPlayerEntity;

public interface ServerPacketHandler<T extends Packet>
{
	void handle(T packet, ServerPlayerEntity player, PacketSender responseSender);
}
