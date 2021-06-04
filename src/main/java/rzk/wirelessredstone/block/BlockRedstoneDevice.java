package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BlockRedstoneDevice extends Block
{
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockRedstoneDevice(Material material)
    {
        super(material);
        setDefaultState(blockState.getBaseState().withProperty(POWERED, false));
    }

    protected boolean isInputSide(IBlockState state, EnumFacing side)
    {
        return false;
    }

    protected boolean isOutputSide(IBlockState state, EnumFacing side)
    {
        return false;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side)
    {
        if (side != null)
            return isInputSide(state, side.getOpposite()) || isOutputSide(state, side.getOpposite());

        return false;
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    protected int getInputPower(World world, BlockPos pos, EnumFacing side)
    {
        BlockPos blockpos = pos.offset(side);
        int i = world.getRedstonePower(blockpos, side);
        if (i >= 15) return 15;

        IBlockState state = world.getBlockState(blockpos);
        return Math.max(i, state.getBlock().equals(Blocks.REDSTONE_WIRE) ? state.getValue(BlockRedstoneWire.POWER) : 0);
    }

    protected boolean isGettingPowered(World world, BlockPos pos, EnumFacing... sides)
    {
        if (sides == null || sides.length == 0)
            return isGettingPowered(world, pos, EnumFacing.values());

        for (EnumFacing side : sides)
            if (getInputPower(world, pos, side) > 0)
                return true;

        return false;
    }

    protected int getOutputPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return state.getValue(POWERED) && isOutputSide(state, side) ? 15 : 0;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return getOutputPower(state, world, pos, side.getOpposite());
    }

    @Override
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return getOutputPower(state, world, pos, side.getOpposite());
    }

    protected void updateNeighborsInFront(IBlockState state, World world, BlockPos pos, EnumFacing side)
    {
        BlockPos blockpos = pos.offset(side);
        if (ForgeEventFactory.onNeighborNotify(world, pos, world.getBlockState(pos), EnumSet.of(side), false).isCanceled())
            return;
        world.neighborChanged(blockpos, this, pos);
        world.notifyNeighborsOfStateExcept(blockpos, this, side.getOpposite());
    }

    public void setPoweredState(IBlockState state, World world, BlockPos pos, boolean powered)
    {
        if (state.getBlock() instanceof BlockRedstoneDevice)
        {
            world.setBlockState(pos, state.withProperty(POWERED, powered));
            for (EnumFacing side : EnumFacing.values())
                if (isOutputSide(state, side))
                    updateNeighborsInFront(state, world, pos, side);
        }
    }

    protected void onInputChanged(IBlockState state, World world, BlockPos pos, EnumFacing side) {}

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos neighbor)
    {
        EnumFacing side = EnumFacing.getFacingFromVector(neighbor.getX() - pos.getX(), neighbor.getY() - pos.getY(), neighbor.getZ() - pos.getZ());
        if (isInputSide(state, side))
            onInputChanged(state, world, pos, side);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(POWERED, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(POWERED) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, POWERED);
    }
}
