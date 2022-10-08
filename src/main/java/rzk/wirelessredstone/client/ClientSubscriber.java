package rzk.wirelessredstone.client;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rzk.wirelessredstone.client.renderer.RedstoneTransceiverBER;
import rzk.wirelessredstone.client.renderer.SnifferHighlightRenderer;
import rzk.wirelessredstone.registries.ModBlockEntities;

public class ClientSubscriber
{
	public static void clientSetup(FMLClientSetupEvent event)
	{
		MinecraftForge.EVENT_BUS.register(SnifferHighlightRenderer.class);
	}

	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerBlockEntityRenderer(ModBlockEntities.REDSTONE_TRANSMITTER_BLOCK_ENTITY_TYPE.get(), RedstoneTransceiverBER::new);
		event.registerBlockEntityRenderer(ModBlockEntities.REDSTONE_RECEIVER_BLOCK_ENTITY_TYPE.get(), RedstoneTransceiverBER::new);
	}
}
