package rzk.wirelessredstone.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;
import rzk.wirelessredstone.tile.TileFrequency;
import rzk.wirelessredstone.tile.TileReceiver;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockReceiver extends BlockFrequency
{
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (!world.isRemote)
		{
			RedstoneNetwork network = RedstoneNetwork.get(world);
			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof TileFrequency)
				network.addDevice((TileFrequency) tile);
		}
	}

	@Override
	protected boolean isOutputSide(IBlockState state, EnumFacing side)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileReceiver();
	}
}
