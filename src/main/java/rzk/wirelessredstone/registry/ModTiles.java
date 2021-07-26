package rzk.wirelessredstone.registry;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.tile.TileFrequency;

import java.util.Collections;

public class ModTiles
{
	private static final ObjectList<BlockEntityType<?>> TILES = new ObjectArrayList<>();

	public static BlockEntityType<TileFrequency.Transmitter> transmitter;
	public static BlockEntityType<TileFrequency.Receiver> receiver;

	private static void initTiles()
	{
		transmitter = registerTile("transmitter", new BlockEntityType<>(TileFrequency.Transmitter::new,
				Collections.singleton(ModBlocks.redstoneTransmitter), null));
		receiver = registerTile("receiver", new BlockEntityType<>(TileFrequency.Receiver::new,
				Collections.singleton(ModBlocks.redstoneReceiver), null));
	}

	public static <T extends BlockEntity> BlockEntityType<T> registerTile(String name, BlockEntityType<T> tileType)
	{
		tileType.setRegistryName(WirelessRedstone.MOD_ID, "tile_" + name);
		TILES.add(tileType);
		return tileType;
	}

	@SubscribeEvent
	public static void registerTiles(RegistryEvent.Register<BlockEntityType<?>> event)
	{
		initTiles();
		TILES.forEach(event.getRegistry()::register);
	}
}
