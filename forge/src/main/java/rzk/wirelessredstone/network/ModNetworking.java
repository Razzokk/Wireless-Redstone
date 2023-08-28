package rzk.wirelessredstone.network;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import rzk.wirelessredstone.WirelessRedstone;

public class ModNetworking
{
	private static int id = 0;
	private static final String PROTOCOL_VERSION = "1.0";

	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
		WirelessRedstone.identifier("main"),
		() -> PROTOCOL_VERSION,
		PROTOCOL_VERSION::equals,
		PROTOCOL_VERSION::equals
	);

	private static int id()
	{
		return id++;
	}

	public static void registerMessages()
	{
		ModNetworking.INSTANCE.messageBuilder(FrequencyBlockPacket.SetFrequency.class, id(), NetworkDirection.PLAY_TO_SERVER)
			.encoder(FrequencyBlockPacket.SetFrequency::write)
			.decoder(FrequencyBlockPacket.SetFrequency::new)
			.consumer(FrequencyBlockPacket.SetFrequency::handle)
			.add();

		ModNetworking.INSTANCE.messageBuilder(FrequencyItemPacket.SetFrequency.class, id(), NetworkDirection.PLAY_TO_SERVER)
			.encoder(FrequencyItemPacket.SetFrequency::write)
			.decoder(FrequencyItemPacket.SetFrequency::new)
			.consumer(FrequencyItemPacket.SetFrequency::handle)
			.add();

		ModNetworking.INSTANCE.messageBuilder(FrequencyBlockPacket.OpenScreen.class, id(), NetworkDirection.PLAY_TO_CLIENT)
			.encoder(FrequencyBlockPacket.OpenScreen::write)
			.decoder(FrequencyBlockPacket.OpenScreen::new)
			.consumer(FrequencyBlockPacket.OpenScreen::handle)
			.add();

		ModNetworking.INSTANCE.messageBuilder(FrequencyItemPacket.OpenScreen.class, id(), NetworkDirection.PLAY_TO_CLIENT)
			.encoder(FrequencyItemPacket.OpenScreen::write)
			.decoder(FrequencyItemPacket.OpenScreen::new)
			.consumer(FrequencyItemPacket.OpenScreen::handle)
			.add();

		ModNetworking.INSTANCE.messageBuilder(SnifferHighlightPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
			.encoder(SnifferHighlightPacket::write)
			.decoder(SnifferHighlightPacket::new)
			.consumer(SnifferHighlightPacket::handle)
			.add();
	}
}
