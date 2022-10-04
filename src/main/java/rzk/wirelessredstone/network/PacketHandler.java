package rzk.wirelessredstone.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import rzk.wirelessredstone.WirelessRedstone;

import java.util.function.Function;

public class PacketHandler
{
	public static final String PROTOCOL_VERSION = "1.0";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(WirelessRedstone.MODID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	private static int id = 0;

	public static void registerMessages()
	{
		registerMessage(FrequencyItemPacket.class, FrequencyItemPacket::new);
		registerMessage(FrequencyBlockPacket.class, FrequencyBlockPacket::new);
	}

	private static <T extends IPacket> void registerMessage(Class<T> messageType, Function<FriendlyByteBuf, T> decoder)
	{
		INSTANCE.registerMessage(id++, messageType, IPacket::encode, decoder, IPacket::handle);
	}
}
