package rzk.wirelessredstone.registry;

import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.SimpleChannel;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.SnifferHighlightPacket;

public class ModNetworking
{
	public static final SimpleChannel INSTANCE = ChannelBuilder
		.named(WirelessRedstone.identifier("main"))
		.simpleChannel();

	public static void registerMessages()
	{
		INSTANCE.messageBuilder(FrequencyBlockPacket.class)
			.encoder(FrequencyBlockPacket::write)
			.decoder(FrequencyBlockPacket::new)
			.consumerMainThread(FrequencyBlockPacket::handle)
			.add();

		INSTANCE.messageBuilder(FrequencyItemPacket.class)
			.encoder(FrequencyItemPacket::write)
			.decoder(FrequencyItemPacket::new)
			.consumerMainThread(FrequencyItemPacket::handle)
			.add();

		INSTANCE.messageBuilder(SnifferHighlightPacket.class, NetworkDirection.PLAY_TO_CLIENT)
			.encoder(SnifferHighlightPacket::write)
			.decoder(SnifferHighlightPacket::new)
			.consumerMainThread(SnifferHighlightPacket::handle)
			.add();
	}
}
