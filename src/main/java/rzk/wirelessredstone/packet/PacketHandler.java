package rzk.wirelessredstone.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import rzk.lib.mc.packet.Packet;
import rzk.wirelessredstone.WirelessRedstone;

import java.util.function.Function;

public class PacketHandler
{
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(WirelessRedstone.MODID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);
	private static int id = 0;

	public static void registerMessages()
	{
		registerMessage(PacketFrequencyBlock.class, PacketFrequencyBlock::new);
		registerMessage(PacketFrequencyItem.class, PacketFrequencyItem::new);
	}

	public static <P extends Packet> void registerMessage(Class<P> packetType, Function<PacketBuffer, P> decoder)
	{
		INSTANCE.registerMessage(id++, packetType, Packet::toBytes, decoder, Packet::handle);
	}
}
