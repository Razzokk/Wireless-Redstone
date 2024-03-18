package rzk.wirelessredstone;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import rzk.wirelessredstone.block.entity.ModBlockEntities;
import rzk.wirelessredstone.item.ModItems;
import rzk.wirelessredstone.item.SnifferItem;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.SnifferHighlightPacket;
import rzk.wirelessredstone.render.RedstoneTransceiverBER;
import rzk.wirelessredstone.render.SnifferHighlightRenderer;
import rzk.wirelessredstone.screen.FrequencyBlockScreen;
import rzk.wirelessredstone.screen.FrequencyItemScreen;

@Environment(EnvType.CLIENT)
public class WirelessRedstoneClientFabric implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		BlockEntityRendererFactories.register(ModBlockEntities.redstoneTransmitterBlockEntityType, RedstoneTransceiverBER::new);
		BlockEntityRendererFactories.register(ModBlockEntities.redstoneReceiverBlockEntityType, RedstoneTransceiverBER::new);

		WorldRenderEvents.AFTER_TRANSLUCENT.register(SnifferHighlightRenderer::renderSnifferHighlights);

		ClientPlayNetworking.registerGlobalReceiver(SnifferHighlightPacket.TYPE, (packet, player, responseSender) ->
		{
			ItemStack stack = player.getStackInHand(packet.hand());
			if (stack.getItem() instanceof SnifferItem item)
				item.setHighlightedBlocks(packet.timestamp(), stack, packet.coords());
		});

		ClientPlayNetworking.registerGlobalReceiver(FrequencyBlockPacket.TYPE, (packet, player, responseSender) ->
			MinecraftClient.getInstance().setScreen(new FrequencyBlockScreen(packet.frequency(), packet.pos())));

		ClientPlayNetworking.registerGlobalReceiver(FrequencyItemPacket.TYPE, (packet, player, responseSender) ->
			MinecraftClient.getInstance().setScreen(new FrequencyItemScreen(packet.frequency(), packet.hand())));

		ModelPredicateProviderRegistry.register(ModItems.remote, new Identifier("state"),
			(stack, world, entity, seed) -> ((entity != null && stack == entity.getActiveItem()) ? 1f : 0f));
	}
}
