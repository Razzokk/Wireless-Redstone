package rzk.wirelessredstone.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.wirelessredstone.RedstoneNetwork;
import rzk.wirelessredstone.tile.TileFrequency;
import rzk.wirelessredstone.util.FrequencyType;

import javax.annotation.Nullable;

public class BlockFrequency extends BlockRedstoneDevice implements ITileEntityProvider
{
    private final FrequencyType type;

    public BlockFrequency(FrequencyType type)
    {
        super(Material.CIRCUITS);
        this.type = type;
    }

    public void updateReceiver(World world, BlockPos pos, boolean powered)
    {
        if (!world.isRemote)
            world.setBlockState(pos, getDefaultState().withProperty(POWERED, powered));
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
                setPoweredState(state, world, pos, true);
                network.addTransmitter(pos, (short) 0);
            }
            else if (isReceiver())
            {
                network.addReceiver(pos, (short) 0);
            }
        }
    }

    @Override
    protected void onInputChanged(IBlockState state, World world, BlockPos pos, EnumFacing side)
    {
        if (isTransmitter() && !world.isRemote)
        {
            boolean powered = isPowered(world, pos, side);

            if (powered != state.getValue(POWERED))
            {
                RedstoneNetwork network = RedstoneNetwork.getOrCreate(world);
                TileEntity tile = world.getTileEntity(pos);
                setPoweredState(state, world, pos, powered);

                if (tile instanceof TileFrequency)
                {
                    short frequency = ((TileFrequency) tile).getFrequency();

                    if (powered)
                        network.addTransmitter(pos, frequency);
                    else
                        network.removeTransmitter(pos, frequency);
                }
            }
        }
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        super.harvestBlock(world, player, pos, state, te, stack);

        RedstoneNetwork network = RedstoneNetwork.getOrCreate(world);
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileFrequency)
        {
            short frequency = ((TileFrequency) tile).getFrequency();

            if (isTransmitter() && world.getBlockState(pos).getValue(POWERED))
                network.removeTransmitter(pos, frequency);
            else  if (isReceiver())
                network.removeReceiver(pos, frequency);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileFrequency(type);
    }

    private boolean isTransmitter()
    {
        return type == FrequencyType.TRANSMITTER;
    }

    private boolean isReceiver()
    {
        return type == FrequencyType.RECEIVER;
    }
}
