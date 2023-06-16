package rzk.wirelessredstone.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import rzk.wirelessredstone.block.entity.ModBlockEntities;
import rzk.wirelessredstone.client.render.RedstoneTransceiverBER;
import rzk.wirelessredstone.client.render.SnifferHighlightRenderer;
import rzk.wirelessredstone.item.SnifferItem;
import rzk.wirelessredstone.network.SnifferHighlightPacket;

@Environment(EnvType.CLIENT)
public class WirelessRedstoneClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		BlockEntityRendererFactories.register(ModBlockEntities.REDSTONE_TRANSMITTER_BLOCK_ENTITY, RedstoneTransceiverBER::new);
		BlockEntityRendererFactories.register(ModBlockEntities.REDSTONE_RECEIVER_BLOCK_ENTITY, RedstoneTransceiverBER::new);

		WorldRenderEvents.AFTER_TRANSLUCENT.register(SnifferHighlightRenderer::renderSnifferHighlights);

		ClientPlayNetworking.registerGlobalReceiver(SnifferHighlightPacket.ID, (client, handler, buf, responseSender) ->
		{
			SnifferHighlightPacket packet = new SnifferHighlightPacket(buf);
			client.execute(() ->
			{
				PlayerEntity player = client.player;
				ItemStack stack = player.getStackInHand(packet.hand);
				if (stack.getItem() instanceof SnifferItem item)
					item.setHighlightedBlocks(packet.timestamp, stack, packet.coords);
			});
		});
	}
}
