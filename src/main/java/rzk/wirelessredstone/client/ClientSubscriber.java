package rzk.wirelessredstone.client;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.client.render.TERFrequency;
import rzk.wirelessredstone.registry.ModItems;
import rzk.wirelessredstone.registry.ModTiles;

import java.util.OptionalDouble;

@Mod.EventBusSubscriber(modid = WirelessRedstone.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class ClientSubscriber
{
	public static final RenderType LINES = RenderType.create("wr_line_render", DefaultVertexFormats.POSITION_COLOR, 1, 256, RenderType.State.builder()
			.setLineState(new RenderState.LineState(OptionalDouble.empty()))
			.setDepthTestState(new RenderState.DepthTestState("always", 519))
			.createCompositeState(false));

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		ClientRegistry.bindTileEntityRenderer(ModTiles.frequency, TERFrequency::new);

		event.enqueueWork(() -> ItemModelsProperties.register(ModItems.remote, new ResourceLocation(WirelessRedstone.MOD_ID, "powered"),
				(stack, world, entity) -> stack.getOrCreateTag().getBoolean("powered") ? 1 : 0));
	}
}
