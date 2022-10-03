package rzk.wirelessredstone.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.api.IChunkLoadListener;

import java.util.Set;

public class EventHandler
{
	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event)
	{
		ChunkAccess chunk = event.getChunk();
		Set<BlockPos> blockEntityPos = chunk.getBlockEntitiesPos();

		for (BlockPos pos : blockEntityPos)
			if (chunk.getBlockEntity(pos) instanceof IChunkLoadListener listener)
				listener.onChunkLoad();
	}
}
