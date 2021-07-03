package rzk.wirelessredstone.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.simple.SimpleChannel;

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

	public static <P extends Packet> void registerMessage(Class<P> packetType, Function<PacketBuffer, P> decoder)
	{
		instance.messageBuilder(packetType, id++)
				.encoder(Packet::toBytes)
				.decoder(decoder)
				.consumer(Packet::handle)
				.add();
	}

	public static void sendToPlayer(Packet packet, ServerPlayerEntity player)
	{
		instance.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}
}
