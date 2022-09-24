package rzk.wirelessredstone.block;

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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.network.PacketFrequency;
import rzk.wirelessredstone.network.PacketHandler;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;
import rzk.wirelessredstone.tile.TileFrequency;

import javax.annotation.Nullable;

public abstract class BlockFrequency extends BlockRedstoneDevice
{
	public BlockFrequency()
	{
		super(new Material(MapColor.IRON));
		setHardness(0.5f);
		setResistance(5.0f);
		setSoundType(SoundType.METAL);
	}

	@Nullable
	@Override
	public String getHarvestTool(IBlockState state)
	{
		return "pickaxe";
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		world.scheduleUpdate(pos, this, 1);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		if (!world.isRemote)
		{
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof TileFrequency)
			{
				RedstoneNetwork network = RedstoneNetwork.get(world, false);
				if (network != null)
					network.removeDevice((TileFrequency) tile);
			}
		}

		super.breakBlock(world, pos, state);
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

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
}
