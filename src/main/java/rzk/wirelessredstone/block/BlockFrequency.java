package rzk.wirelessredstone.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import rzk.lib.mc.block.BlockRedstoneDevice;
import rzk.lib.mc.util.WorldUtils;
import rzk.wirelessredstone.RedstoneNetwork;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.packet.PacketFrequencyBlock;
import rzk.wirelessredstone.tile.TileFrequency;

import javax.annotation.Nullable;

public class BlockFrequency extends BlockRedstoneDevice
{
	public final boolean isTransmitter;

	public BlockFrequency(boolean isTransmitter)
	{
		super(Properties.create(Material.IRON));
		this.isTransmitter = isTransmitter;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if (world.isRemote)
			WorldUtils.ifTilePresent(world, pos, TileFrequency.class, tile ->
					WirelessRedstone.proxy.openFrequencyGui(tile.getFrequency(), new PacketFrequencyBlock(pos)));
		return ActionResultType.SUCCESS;
	}

	@Override
	protected boolean isInputSide(BlockState state, Direction side)
	{
		return isTransmitter;
	}

	@Override
	protected boolean isOutputSide(BlockState state, Direction side)
	{
		return !isTransmitter;
	}

	@Override
	protected void onInputChanged(BlockState state, World world, BlockPos pos, Direction side)
	{
		if (isTransmitter && !world.isRemote)
		{
			boolean isPowered = isPowered(world, pos);
			if (state.get(BlockStateProperties.POWERED) != isPowered)
			{
				setPoweredState(state, world, pos, isPowered);
				WorldUtils.ifTilePresent(world, pos, TileFrequency.class, tile ->
				{
					RedstoneNetwork network = RedstoneNetwork.getOrCreate(world);
					int frequency = tile.getFrequency();

					if (isPowered)
						network.addActiveTransmitter(frequency);
					else
						network.removeActiveTransmitter(frequency);
				});
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
	{
		if (!world.isRemote & isTransmitter)
			onInputChanged(state, world, pos, null);
	}

	@Override
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (state.getBlock() != newState.getBlock())
		{
			if (!world.isRemote)
			{
				WorldUtils.ifTilePresent(world, pos, TileFrequency.class, tile ->
				{
					RedstoneNetwork network = RedstoneNetwork.getOrCreate(world);
					int frequency = tile.getFrequency();

					if (isTransmitter && state.get(BlockStateProperties.POWERED))
						network.removeActiveTransmitter(frequency);
					else
						network.removeReceiver(frequency, pos);
				});
			}
			super.onReplaced(state, world, pos, newState, isMoving);
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
		return new TileFrequency(isTransmitter);
	}

	@Override
	public BlockItem createItem()
	{
		return new BlockItem(this, new Item.Properties().group(WirelessRedstone.ITEM_GROUP_WIRELESS_REDSTONE));
	}
}
