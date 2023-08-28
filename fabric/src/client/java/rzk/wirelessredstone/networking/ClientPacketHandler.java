package rzk.wirelessredstone.networking;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayerEntity;
import rzk.wirelessredstone.network.packet.Packet;

public interface ClientPacketHandler<T extends Packet>
{
	void handle(T packet, ClientPlayerEntity player, PacketSender responseSender);
}
