package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import rzk.wirelessredstone.client.gui.ClientScreens;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.rsnetwork.Device;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class BlockFrequency extends BlockRedstoneDevice
{
	public BlockFrequency()
	{
		super(Properties.of(Material.METAL)
				.harvestTool(ToolType.PICKAXE)
				.strength(0.5F, 5.0F)
				.sound(SoundType.METAL)
		);
	}

	public static boolean isFreqBlock(BlockState state)
	{
		return state.is(ModBlocks.redstoneReceiver) || state.is(ModBlocks.redstoneTransmitter);
	}

	public static void setPoweredState(World world, BlockPos pos, boolean powered)
	{
		BlockState state = world.getBlockState(pos);

		if (isFreqBlock(state))
			((BlockFrequency) state.getBlock()).setPowered(state, world, pos, powered);
	}

	@Override
	public boolean isSignalSource(BlockState state)
	{
		return false;
	}

	@Override
	protected boolean isInputSide(BlockState state, Direction side)
	{
		return false;
	}

	@Override
	protected boolean isOutputSide(BlockState state, Direction side)
	{
		return false;
	}

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult)
	{
		if (world.isClientSide)
		{
			TileEntity tile = world.getBlockEntity(pos);

			if (tile instanceof Device.Block)
				ClientScreens.openFreqGui((Device.Block) tile);
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public void onPlace(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		if (!world.isClientSide)
			world.getBlockTicks().scheduleTick(pos, this, 1);
	}

	@Override
	protected void onInputChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos neighbour, Direction side) {}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Nullable
	@Override
	public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);
}
