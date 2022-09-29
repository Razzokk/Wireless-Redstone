package rzk.wirelessredstone.registry;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.tile.TileFrequencyOld;
import rzk.wirelessredstone.tile.TileReceiver;
import rzk.wirelessredstone.tile.TileTransmitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public final class ModTiles
{
	private static final List<TileEntityType<?>> TILE_TYPE_CACHE = new ArrayList<>();

	private ModTiles() {}

	public static TileEntityType<TileTransmitter> redstoneTransmitter;
	public static TileEntityType<TileReceiver> redstoneReceiver;
	// TODO: remove, this is only here for backwards-compat
	public static TileEntityType<TileFrequencyOld> frequency;

	private static void initTiles()
	{
		redstoneTransmitter = registerTileType("redstone_transmitter", TileTransmitter::new, ModBlocks.redstoneTransmitter);
		redstoneReceiver = registerTileType("redstone_receiver", TileReceiver::new, ModBlocks.redstoneReceiver);
		frequency = registerTileType("tile_frequency", TileFrequencyOld::new, ModBlocks.redstoneTransmitter, ModBlocks.redstoneReceiver);
	}

	private static <T extends TileEntity> TileEntityType<T> registerTileType(String name, TileEntityType<T> tileType)
	{
		tileType.setRegistryName(WirelessRedstone.MOD_ID, name);
		TILE_TYPE_CACHE.add(tileType);
		return tileType;
	}

	private static <T extends TileEntity> TileEntityType<T> registerTileType(String name, Supplier<T> tileSupplier, Block... validBlocks)
	{
		Set<Block> blocks = new HashSet<>(Arrays.asList(validBlocks));
		return registerTileType(name, new TileEntityType<>(tileSupplier, blocks, null));
	}

	@SubscribeEvent
	public static void registerTiles(RegistryEvent.Register<TileEntityType<?>> event)
	{
		initTiles();
		IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
		TILE_TYPE_CACHE.forEach(registry::register);
		TILE_TYPE_CACHE.clear();
	}
}
