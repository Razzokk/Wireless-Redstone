package rzk.wirelessredstone.block;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;
import rzk.wirelessredstone.tile.TileFrequency;
import rzk.wirelessredstone.tile.TileReceiver;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockReceiver extends BlockFrequency
{
	@Override
	public boolean isSignalSource(BlockState state)
	{
		return true;
	}

	@Override
	protected boolean isOutputSide(BlockState state, Direction side)
	{
		return true;
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
	{
		RedstoneNetwork network = RedstoneNetwork.get(world);
		TileEntity tile = world.getBlockEntity(pos);

		if (tile instanceof TileFrequency)
			network.addDevice((TileFrequency) tile);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileReceiver();
	}
}
