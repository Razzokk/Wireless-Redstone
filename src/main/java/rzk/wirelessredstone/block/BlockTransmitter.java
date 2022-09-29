package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import rzk.wirelessredstone.rsnetwork.Device;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;
import rzk.wirelessredstone.tile.TileFrequency;
import rzk.wirelessredstone.tile.TileTransmitter;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTransmitter extends BlockFrequency
{
	@Override
	protected boolean isInputSide(BlockState state, Direction side)
	{
		return true;
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
	{
		RedstoneNetwork network = RedstoneNetwork.get(world);
		TileEntity tile = world.getBlockEntity(pos);

		if (!isGettingPowered(state, world, pos)) return;

		setPowered(state, world, pos, true);

		if (tile instanceof TileFrequency)
			network.addDevice((TileFrequency) tile);
	}

	@Override
	protected void onInputChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighbour, Direction side)
	{
		if (world.isClientSide || !shouldUpdate(state, world, pos)) return;

		boolean powered = isGettingPowered(state, world, pos);
		setPowered(state, world, pos, powered);
		TileEntity tile = world.getBlockEntity(pos);

		if (!(tile instanceof Device)) return;

		Device device = (Device) tile;
		RedstoneNetwork network = RedstoneNetwork.get((ServerWorld) world);

		if (network == null) return;

		if (powered) network.addDevice(device);
		else network.removeDevice(device);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileTransmitter();
	}
}
