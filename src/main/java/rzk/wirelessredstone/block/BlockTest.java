package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.wirelessredstone.RedstoneNetwork;

public class BlockTest extends Block
{
    public BlockTest()
    {
        super(Material.CIRCUITS);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        if (!world.isRemote)
            RedstoneNetwork.getOrCreate(world).placeBlock();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (player.isSneaking())
            return false;

        if (!world.isRemote)
            System.out.println(RedstoneNetwork.getOrCreate(world).getPlacedBlocks());

        return true;
    }
}
