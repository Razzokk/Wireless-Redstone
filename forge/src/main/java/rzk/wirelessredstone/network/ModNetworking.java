package rzk.wirelessredstone.network;

import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.SimpleChannel;
import rzk.wirelessredstone.WirelessRedstone;

public class ModNetworking
{
	public static final SimpleChannel INSTANCE = ChannelBuilder
		.named(WirelessRedstone.identifier("main"))
		.simpleChannel();

	public static void registerMessages()
	{
		ModNetworking.INSTANCE.messageBuilder(FrequencyBlockPacket.SetFrequency.class, NetworkDirection.PLAY_TO_SERVER)
			.encoder(FrequencyBlockPacket.SetFrequency::write)
			.decoder(FrequencyBlockPacket.SetFrequency::new)
			.consumerMainThread(FrequencyBlockPacket.SetFrequency::handle)
			.add();

		ModNetworking.INSTANCE.messageBuilder(FrequencyItemPacket.SetFrequency.class, NetworkDirection.PLAY_TO_SERVER)
			.encoder(FrequencyItemPacket.SetFrequency::write)
			.decoder(FrequencyItemPacket.SetFrequency::new)
			.consumerMainThread(FrequencyItemPacket.SetFrequency::handle)
			.add();

		ModNetworking.INSTANCE.messageBuilder(FrequencyBlockPacket.OpenScreen.class, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(FrequencyBlockPacket.OpenScreen::write)
			.decoder(FrequencyBlockPacket.OpenScreen::new)
			.consumerMainThread(FrequencyBlockPacket.OpenScreen::handle)
			.add();

		ModNetworking.INSTANCE.messageBuilder(FrequencyItemPacket.OpenScreen.class, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(FrequencyItemPacket.OpenScreen::write)
			.decoder(FrequencyItemPacket.OpenScreen::new)
			.consumerMainThread(FrequencyItemPacket.OpenScreen::handle)
			.add();

		ModNetworking.INSTANCE.messageBuilder(SnifferHighlightPacket.class, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(SnifferHighlightPacket::write)
			.decoder(SnifferHighlightPacket::new)
			.consumerMainThread(SnifferHighlightPacket::handle)
			.add();
	}
}
