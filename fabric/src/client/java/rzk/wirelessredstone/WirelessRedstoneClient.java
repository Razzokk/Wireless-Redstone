package rzk.wirelessredstone;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.Identifier;
import rzk.wirelessredstone.block.entity.ModBlockEntities;
import rzk.wirelessredstone.item.ModItems;
import rzk.wirelessredstone.networking.ModClientNetworking;
import rzk.wirelessredstone.render.RedstoneTransceiverBER;
import rzk.wirelessredstone.render.SnifferHighlightRenderer;

@Environment(EnvType.CLIENT)
public class WirelessRedstoneClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		BlockEntityRendererRegistry.register(ModBlockEntities.REDSTONE_TRANSMITTER_BLOCK_ENTITY, RedstoneTransceiverBER::new);
		BlockEntityRendererRegistry.register(ModBlockEntities.REDSTONE_RECEIVER_BLOCK_ENTITY, RedstoneTransceiverBER::new);

		WorldRenderEvents.AFTER_TRANSLUCENT.register(SnifferHighlightRenderer::renderSnifferHighlights);
		ModClientNetworking.register();

		ModelPredicateProviderRegistry.register(ModItems.REMOTE, new Identifier("state"),
			(stack, world, entity, seed) -> ((entity != null && stack == entity.getActiveItem()) ? 1f : 0f));
	}
}
