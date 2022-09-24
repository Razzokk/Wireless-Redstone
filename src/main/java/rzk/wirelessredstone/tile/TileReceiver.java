package rzk.wirelessredstone.tile;

import net.minecraft.block.state.IBlockState;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;

public class TileReceiver extends TileFrequency
{
	@Override
	public Type getType()
	{
		return Type.RECEIVER;
	}

	@Override
	public void onChunkLoad()
	{
		if (!world.isRemote)
		{
			RedstoneNetwork network = RedstoneNetwork.get(world);
			IBlockState state = world.getBlockState(pos);
			((BlockFrequency) getBlockType()).setPoweredState(state, world, pos, network.isChannelActive(getFrequency()));
		}
	}
}
