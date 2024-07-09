package rzk.wirelessredstone.client;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rzk.wirelessredstone.client.render.WRWorldRendererForge;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModItems;
import rzk.wirelessredstone.render.RedstoneTransceiverBER;

public class WirelessRedstoneClientForge
{
	public static void clientSetup(FMLClientSetupEvent event)
	{
		MinecraftForge.EVENT_BUS.register(WRWorldRendererForge.class);

		event.enqueueWork(() -> ModelPredicateProviderRegistry.register(ModItems.remote, new Identifier("state"),
			(stack, world, entity, seed) -> ((entity != null && stack == entity.getActiveItem()) ? 1f : 0f)));
	}

	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerBlockEntityRenderer(ModBlockEntities.redstoneTransmitterBlockEntityType, RedstoneTransceiverBER::new);
		event.registerBlockEntityRenderer(ModBlockEntities.redstoneReceiverBlockEntityType, RedstoneTransceiverBER::new);
	}
}
