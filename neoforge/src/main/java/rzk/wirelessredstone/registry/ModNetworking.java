package rzk.wirelessredstone.registry;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.SnifferHighlightPacket;

public class ModNetworking
{
	@SubscribeEvent
	public static void register(RegisterPayloadHandlerEvent event)
	{
		var registrar = event.registrar(WirelessRedstone.MODID);

		registrar.play(FrequencyBlockPacket.ID, FrequencyBlockPacket::new, builder -> builder
			.server(FrequencyBlockPacket::handleServer)
			.client(FrequencyBlockPacket::handleClient));

		registrar.play(FrequencyItemPacket.ID, FrequencyItemPacket::new, builder -> builder
			.server(FrequencyItemPacket::handleServer)
			.client(FrequencyItemPacket::handleClient));

		registrar.play(SnifferHighlightPacket.ID, SnifferHighlightPacket::new, builder -> builder
			.client(SnifferHighlightPacket::handle));
	}
}
