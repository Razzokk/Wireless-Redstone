package rzk.wirelessredstone.registry;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.lib.mc.registry.ModRegistry;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.tile.TileFrequency;

import java.util.ArrayList;
import java.util.List;

public class ModTiles
{
	public static final List<TileEntityType<?>> TILES = new ArrayList<>();

	@SubscribeEvent
	public static void registerTiles(RegistryEvent.Register<TileEntityType<?>> event)
	{
		registerTile(TileFrequency.TYPE, "tile_frequency");

		TILES.forEach(event.getRegistry()::register);
	}

	public static TileEntityType<?> registerTile(TileEntityType<?> tile, String name)
	{
		return ModRegistry.registerTile(WirelessRedstone.MODID, TILES, tile, name);
	}
}
