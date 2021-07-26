package rzk.wirelessredstone.client;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.client.render.TERFrequency;
import rzk.wirelessredstone.registry.ModItems;
import rzk.wirelessredstone.registry.ModTiles;

@Mod.EventBusSubscriber(modid = WirelessRedstone.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ClientSubscriber
{
	/*public static final RenderType LINES = RenderType.create("wr_line_render", DefaultVertexFormat.POSITION_COLOR, 1, 256, RenderType.CompositeState.builder()
			.setLineState(new RenderS.LineState(OptionalDouble.empty()))
			.setDepthTestState(new RenderState.DepthTestState("always", 519))
			.createCompositeState(false));*/

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		BlockEntityRenderers.register(ModTiles.transmitter, TERFrequency::new);
		BlockEntityRenderers.register(ModTiles.receiver, TERFrequency::new);

		event.enqueueWork(() -> ItemProperties.register(ModItems.remote, new ResourceLocation(WirelessRedstone.MOD_ID, "powered"),
				(stack, world, entity, var4) -> stack.getOrCreateTag().getBoolean("powered") ? 1 : 0));
	}
}
