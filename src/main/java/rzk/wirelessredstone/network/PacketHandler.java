package rzk.wirelessredstone.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

import java.util.function.Function;

public final class PacketHandler
{
	public static final String PROTOCOL_VERSION = "1.0";
	public static SimpleChannel instance;
	private static int id = 0;

	private PacketHandler() {}

	public static void registerMessages()
	{
		registerMessage(PacketSetFrequency.class, PacketSetFrequency::new);
	}

	public static <P extends Packet> void registerMessage(Class<P> packetType, Function<FriendlyByteBuf, P> decoder)
	{
		instance.messageBuilder(packetType, id++)
				.encoder(Packet::toBytes)
				.decoder(decoder)
				.consumer(Packet::handle)
				.add();
	}

	public static void sendToPlayer(Packet packet, ServerPlayer player)
	{
		instance.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}

	public static void sendToServer(Packet packet)
	{
		instance.sendToServer(packet);
	}
}
