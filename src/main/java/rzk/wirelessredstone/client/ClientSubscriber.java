package rzk.wirelessredstone.client;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.client.renderer.RedstoneTransceiverBER;
import rzk.wirelessredstone.registries.ModBlockEntities;

public class ClientSubscriber
{
	@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerBlockEntityRenderer(ModBlockEntities.REDSTONE_TRANSMITTER_BLOCK_ENTITY_TYPE.get(), RedstoneTransceiverBER::new);
		event.registerBlockEntityRenderer(ModBlockEntities.REDSTONE_RECEIVER_BLOCK_ENTITY_TYPE.get(), RedstoneTransceiverBER::new);
	}
}
