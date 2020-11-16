package rzk.wirelessredstone.proxy;

import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rzk.wirelessredstone.client.render.TERFrequency;
import rzk.wirelessredstone.registry.ModItems;
import rzk.wirelessredstone.registry.ModTiles;

public class ClientProxy
{
	private static final IItemPropertyGetter POWERED = (stack, world, entity) ->
			stack.getOrCreateTag().getBoolean("powered") ? 1.0F : 0.0F;

	public static void clientSetup(FMLClientSetupEvent event)
	{
		ClientRegistry.bindTileEntityRenderer(ModTiles.TILE_FREQUENCY.get(), TERFrequency::new);
		ItemModelsProperties.registerProperty(ModItems.REMOTE.get(), new ResourceLocation("powered"), POWERED);
	}
}
