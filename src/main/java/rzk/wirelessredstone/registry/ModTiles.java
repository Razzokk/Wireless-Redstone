package rzk.wirelessredstone.registry;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import rzk.lib.mc.tile.TileType;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.tile.TileFrequency;

public class ModTiles
{
	public static final DeferredRegister<TileEntityType<?>> TILES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, WirelessRedstone.MOD_ID);

	public static final RegistryObject<TileEntityType<TileFrequency>> TILE_FREQUENCY = TILES.register("tile_frequency", () -> new TileType<>(TileFrequency::new));
}
