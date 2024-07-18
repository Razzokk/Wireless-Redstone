package rzk.wirelessredstone.api;

import net.minecraft.server.world.ServerWorld;

public interface ChunkLoadListener
{
	void onChunkLoad(ServerWorld world);

	void onChunkUnload(ServerWorld world);
}
