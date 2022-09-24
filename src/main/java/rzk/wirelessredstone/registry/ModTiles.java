package rzk.wirelessredstone.registry;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.tile.TileReceiver;
import rzk.wirelessredstone.tile.TileTransmitter;

public final class ModTiles
{
	private ModTiles() {}

	private static void registerTile(String name, Class<? extends TileEntity> tileClass)
	{
		GameRegistry.registerTileEntity(tileClass, new ResourceLocation(WirelessRedstone.MOD_ID, "tileentity." + name));
	}

	@SubscribeEvent
	public static void registerTiles(RegistryEvent.Register<Block> event)
	{
		registerTile("transmitter", TileTransmitter.class);
		registerTile("receiver", TileReceiver.class);
	}
}
