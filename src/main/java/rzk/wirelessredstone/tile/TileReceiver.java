package rzk.wirelessredstone.tile;

import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.world.server.ServerWorld;
import rzk.wirelessredstone.api.IChunkLoadListener;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.registry.ModTiles;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;

public class TileReceiver extends TileFrequency implements IChunkLoadListener
{
	public TileReceiver()
	{
		super(ModTiles.redstoneReceiver);
	}

	@Override
	public Type getDeviceType()
	{
		return Type.RECEIVER;
	}

	@Override
	public void onChunkLoad()
	{
		if (!level.isClientSide)
		{
			level.getServer().tell(new TickDelayedTask(1, () ->
			{
				RedstoneNetwork network = RedstoneNetwork.get((ServerWorld) level);
				BlockFrequency.setPoweredState(level, worldPosition, network.isChannelActive(getFrequency()));
			}));
		}
	}
}
