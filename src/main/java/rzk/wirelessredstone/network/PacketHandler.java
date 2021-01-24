package rzk.wirelessredstone.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import rzk.wirelessredstone.WirelessRedstone;

public class PacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(WirelessRedstone.MOD_ID);
	private static int id = 0;

	public static void registerPackets()
	{
		registerPacket(PacketFrequency.PacketFrequencyOpenGuiHandler.class, PacketFrequency.class, Side.CLIENT);
		registerPacket(PacketFrequency.PacketFrequencyHandler.class, PacketFrequency.class, Side.SERVER);
	}

	public static <REQ extends IMessage, REPLY extends IMessage> void registerPacket(Class<? extends IMessageHandler<REQ, REPLY>> packetHandler, Class<REQ> packet, Side side)
	{
		INSTANCE.registerMessage(packetHandler, packet, id++, side);
	}
}
