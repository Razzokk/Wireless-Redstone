package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import rzk.wirelessredstone.rsnetwork.Device;
import rzk.wirelessredstone.tile.TileFrequency;

import javax.annotation.Nullable;

public class BlockFrequency extends BlockRedstoneDevice
{
	private final Device.Type type;

	public BlockFrequency(Device.Type type)
	{
		super(Properties.of(Material.METAL));
		this.type = type;
	}

	@Override
	public boolean isSignalSource(BlockState state)
	{
		return isReceiver();
	}

	@Override
	protected boolean isInputSide(BlockState state, Direction side)
	{
		return isTransmitter();
	}

	@Override
	protected boolean isOutputSide(BlockState state, Direction side)
	{
		return isReceiver();
	}

	@Override
	protected void onInputChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighbour, Direction side)
	{
		if (!world.isClientSide && isTransmitter())
		{
			System.out.println("Here we have to do the redstone network stuff");
			//TODO: implement redstone net stuff
		}
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new TileFrequency(type);
	}

	public boolean isTransmitter()
	{
		return type == Device.Type.TRANSMITTER;
	}

	public boolean isReceiver()
	{
		return type == Device.Type.RECEIVER;
	}
}
