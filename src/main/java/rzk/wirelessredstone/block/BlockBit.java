package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class BlockBit extends BlockRedstoneDevice
{
	public BlockBit()
	{
		super(Properties.of(Material.METAL));
	}

	@Override
	public boolean isSignalSource(BlockState state)
	{
		return true;
	}

	@Override
	protected boolean isInputSide(BlockState state, Direction side)
	{
		return side != Direction.WEST && (side.get3DDataValue() % 2) == 0;
	}

	@Override
	protected boolean isOutputSide(BlockState state, Direction side)
	{
		return (side.get3DDataValue() % 2) != 0;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
	{
		if (!world.isClientSide)
			setPowered(state, world, pos, !isPowered(state));

		return ActionResultType.SUCCESS;
	}

	@Override
	protected void onInputChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighbour, Direction side)
	{
		boolean isGettingPowered = isGettingPowered(state, world, pos);

		if (isPowered(state) != isGettingPowered)
			setPowered(state, world, pos, isGettingPowered);
	}
}
