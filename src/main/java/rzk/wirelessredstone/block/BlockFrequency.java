package rzk.wirelessredstone.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.network.PacketFrequency;
import rzk.wirelessredstone.network.PacketHandler;
import rzk.wirelessredstone.rsnetwork.Device;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;
import rzk.wirelessredstone.tile.TileFrequency;

import javax.annotation.Nullable;

public class BlockFrequency extends BlockRedstoneDevice implements ITileEntityProvider
{
	public final Device.Type type;

	public BlockFrequency(Device.Type type)
	{
		super(new Material(MapColor.IRON));
		setHardness(0.5f);
		setResistance(5.0f);
		setSoundType(SoundType.METAL);
		this.type = type;
	}

	@Nullable
	@Override
	public String getHarvestTool(IBlockState state)
	{
		return "pickaxe";
	}

	@Override
	protected boolean isInputSide(IBlockState state, EnumFacing side)
	{
		return isTransmitter();
	}

	@Override
	protected boolean isOutputSide(IBlockState state, EnumFacing side)
	{
		return isReceiver();
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		if (!world.isRemote)
		{
			RedstoneNetwork network = RedstoneNetwork.get(world);

			if (isTransmitter() && isGettingPowered(world, pos))
			{
				network.addDevice(Device.create((short) 0, type, pos));
				setPoweredState(state, world, pos, true);
			}
			else if (isReceiver())
			{
				network.addDevice(Device.create((short) 0, type, pos));
				setPoweredState(state, world, pos, network.isChannelActive((short) 0));
			}
		}
	}

	@Override
	protected void onInputChanged(IBlockState state, World world, BlockPos pos, BlockPos neighbor, EnumFacing side)
	{
		if (isTransmitter() && !world.isRemote && shouldUpdate(world, pos))
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (player.isSneaking())
			return false;

		if (!world.isRemote)
		{
			TileEntity tile = world.getTileEntity(pos);

			if (tile instanceof TileFrequency)
			{
				boolean extended = player.getEntityData().getBoolean(WirelessRedstone.MOD_ID + ".extended");
				PacketHandler.INSTANCE.sendTo(new PacketFrequency(((TileFrequency) tile).getFrequency(), extended, pos), (EntityPlayerMP) player);
			}
		}
		return true;
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileFrequency(type);
	}

	private boolean isTransmitter()
	{
		return type == Device.Type.TRANSMITTER;
	}

	private boolean isReceiver()
	{
		return type == Device.Type.RECEIVER;
	}
}
