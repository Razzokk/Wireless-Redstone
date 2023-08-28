package rzk.wirelessredstone.client;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rzk.wirelessredstone.block.entity.ModBlockEntities;
import rzk.wirelessredstone.client.render.RedstoneTransceiverBER;
import rzk.wirelessredstone.client.render.SnifferHighlightRenderer;
import rzk.wirelessredstone.item.ModItems;

public class WirelessRedstoneClient
{
	public static void clientSetup(FMLClientSetupEvent event)
	{
		MinecraftForge.EVENT_BUS.register(SnifferHighlightRenderer.class);

		ItemProperties.register(ModItems.REMOTE.get(), new ResourceLocation("state"),
			(stack, world, entity, seed) -> ((entity != null && stack == entity.getUseItem()) ? 1f : 0f));
	}

	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerBlockEntityRenderer(ModBlockEntities.REDSTONE_TRANSMITTER_BLOCK_ENTITY_TYPE.get(), RedstoneTransceiverBER::new);
		event.registerBlockEntityRenderer(ModBlockEntities.REDSTONE_RECEIVER_BLOCK_ENTITY_TYPE.get(), RedstoneTransceiverBER::new);
	}
}
