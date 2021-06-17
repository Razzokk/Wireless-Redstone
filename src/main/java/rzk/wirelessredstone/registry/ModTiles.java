package rzk.wirelessredstone.registry;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.tile.TileFrequency;
import rzk.wirelessredstone.tile.TileType;

public class ModTiles
{
	public static final ObjectList<TileEntityType<?>> TILES = new ObjectArrayList<>();

	public static TileEntityType<TileFrequency> frequency;

	private static void initTiles()
	{
		frequency = registerTile("frequency", new TileType<>(TileFrequency::new));
	}

	public static <T extends TileEntity> TileEntityType<T> registerTile(String name, TileEntityType<T> tileType)
	{
		tileType.setRegistryName(WirelessRedstone.MOD_ID, "tile_" + name);
		TILES.add(tileType);
		return tileType;
	}

	@SubscribeEvent
	public static void registerTiles(RegistryEvent.Register<TileEntityType<?>> event)
	{
		initTiles();
		TILES.forEach(event.getRegistry()::register);
	}
}
