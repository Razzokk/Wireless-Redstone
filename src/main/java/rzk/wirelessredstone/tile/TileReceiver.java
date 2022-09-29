package rzk.wirelessredstone.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.WorldServer;
import rzk.wirelessredstone.api.IChunkLoadListener;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;

public class TileReceiver extends TileFrequency implements IChunkLoadListener
{
	@Override
	public Type getType()
	{
		return Type.RECEIVER;
	}

	@Override
	public void onChunkLoad()
	{
		if (world.isRemote) return;

		((WorldServer) world).addScheduledTask(() ->
		{
			RedstoneNetwork network = RedstoneNetwork.get(world);
			IBlockState state = world.getBlockState(pos);
			((BlockFrequency) getBlockType()).setPoweredState(state, world, pos, network.isChannelActive(getFrequency()));
		});
	}
}
