package rzk.wirelessredstone.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.wirelessredstone.RedstoneNetwork;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.network.PacketFrequency;
import rzk.wirelessredstone.network.PacketHandler;
import rzk.wirelessredstone.tile.TileFrequency;
import rzk.wirelessredstone.util.DeviceType;

import javax.annotation.Nullable;

public class BlockFrequency extends BlockRedstoneDevice implements ITileEntityProvider
{
    public final DeviceType type;

    public BlockFrequency(DeviceType type)
    {
        super(Material.CIRCUITS);
        setHardness(0.5f);
        setSoundType(SoundType.METAL);
        this.type = type;
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
            RedstoneNetwork network = RedstoneNetwork.getOrCreate(world);

            if (isTransmitter() && isPowered(world, pos))
            {
                network.addDevice((short) 0, pos, type);
                setPoweredState(state, world, pos, true);
            }
            else if (isReceiver())
            {
                network.addDevice((short) 0, pos, type);
                setPoweredState(state, world, pos, network.isChannelActive((short) 0));
            }
        }
    }

    @Override
    protected void onInputChanged(IBlockState state, World world, BlockPos pos, EnumFacing side)
    {
        if (isTransmitter() && !world.isRemote)
        {
            boolean powered = isPowered(world, pos);

            if (powered != state.getValue(POWERED))
            {
                setPoweredState(state, world, pos, powered);
                TileEntity tile = world.getTileEntity(pos);

                if (tile instanceof TileFrequency)
                {
                    RedstoneNetwork network = RedstoneNetwork.getOrCreate(world);
                    short frequency = ((TileFrequency) tile).getFrequency();

                    if (powered)
                        network.addDevice(frequency, pos, DeviceType.TRANSMITTER);
                    else
                        network.removeDevice(frequency, pos, DeviceType.TRANSMITTER);
                }
            }
        }
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof TileFrequency)
            {
                RedstoneNetwork network = RedstoneNetwork.getOrCreate(world);
                short frequency = ((TileFrequency) tile).getFrequency();
                network.removeDevice(frequency, pos, type);
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
        return type == DeviceType.TRANSMITTER;
    }

    private boolean isReceiver()
    {
        return type == DeviceType.RECEIVER;
    }
}
