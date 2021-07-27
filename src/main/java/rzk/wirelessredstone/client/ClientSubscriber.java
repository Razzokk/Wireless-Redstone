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
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		BlockEntityRenderers.register(ModTiles.transmitter, TERFrequency::new);
		BlockEntityRenderers.register(ModTiles.receiver, TERFrequency::new);

		event.enqueueWork(() -> ItemProperties.register(ModItems.remote, new ResourceLocation(WirelessRedstone.MOD_ID, "powered"),
				(stack, world, entity, var4) -> stack.getOrCreateTag().getBoolean("powered") ? 1 : 0));
	}
}
