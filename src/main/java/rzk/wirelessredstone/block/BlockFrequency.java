package rzk.wirelessredstone.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
        if (isTransmitter() && !world.isRemote && isPowered(world, pos))
            setPoweredState(state, world, pos, true);
    }

    @Override
    protected void onInputChanged(IBlockState state, World world, BlockPos pos, EnumFacing side)
    {
        if (isTransmitter() && !world.isRemote)
        {
            boolean powered = isPowered(world, pos, side);

            if (powered != state.getValue(POWERED))
                setPoweredState(state, world, pos, powered);
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
