package rzk.wirelessredstone.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import rzk.wirelessredstone.WirelessRedstone;

public class PacketHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(WirelessRedstone.MOD_ID);
	private static int id = 0;

	public static void registerPackets()
	{
		INSTANCE.registerMessage(PacketFrequency.PacketFrequencyOpenGuiHandler.class, PacketFrequency.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(PacketFrequency.PacketFrequencyHandler.class, PacketFrequency.class, id++, Side.SERVER);
	}
}
