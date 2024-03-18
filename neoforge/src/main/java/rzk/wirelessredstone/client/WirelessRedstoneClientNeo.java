package rzk.wirelessredstone.client;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;
import rzk.wirelessredstone.block.entity.ModBlockEntities;
import rzk.wirelessredstone.client.render.SnifferHighlightRenderer;
import rzk.wirelessredstone.item.ModItems;
import rzk.wirelessredstone.render.RedstoneTransceiverBER;

public class WirelessRedstoneClientNeo
{
	public static void clientSetup(FMLClientSetupEvent event)
	{
		NeoForge.EVENT_BUS.register(SnifferHighlightRenderer.class);

		ModelPredicateProviderRegistry.register(ModItems.remote, new Identifier("state"),
			(stack, world, entity, seed) -> ((entity != null && stack == entity.getActiveItem()) ? 1f : 0f));
	}

	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerBlockEntityRenderer(ModBlockEntities.redstoneTransmitterBlockEntityType, RedstoneTransceiverBER::new);
		event.registerBlockEntityRenderer(ModBlockEntities.redstoneReceiverBlockEntityType, RedstoneTransceiverBER::new);
	}
}
