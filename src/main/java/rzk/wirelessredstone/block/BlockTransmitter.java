package rzk.wirelessredstone.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.wirelessredstone.rsnetwork.Device;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;
import rzk.wirelessredstone.tile.TileFrequency;
import rzk.wirelessredstone.tile.TileTransmitter;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTransmitter extends BlockFrequency
{
	@Override
	protected void onInputChanged(IBlockState state, World world, BlockPos pos, BlockPos neighbor, EnumFacing side)
	{
		if (!world.isRemote && shouldUpdate(world, pos))
		{
			boolean powered = isGettingPowered(world, pos);
			setPoweredState(state, world, pos, powered);
			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof Device)
			{
				Device device = (Device) tile;
				RedstoneNetwork network = RedstoneNetwork.get(world);

				if (powered)
					network.addDevice(device);
				else
					network.removeDevice(device);
			}
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		if (!world.isRemote)
		{
			RedstoneNetwork network = RedstoneNetwork.get(world);
			TileEntity tile = world.getTileEntity(pos);

			if (isGettingPowered(world, pos))
			{
				setPoweredState(state, world, pos, true);
				if (tile instanceof TileFrequency)
					network.addDevice((TileFrequency) tile);
			}
		}
	}

	@Override
	protected boolean isInputSide(IBlockState state, EnumFacing side)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileTransmitter();
	}
}
