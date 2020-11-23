package rzk.wirelessredstone.block;

import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import rzk.lib.mc.block.BlockRedstoneDevice;
import rzk.lib.mc.util.ObjectUtils;
import rzk.lib.mc.util.WorldUtils;
import rzk.wirelessredstone.RedstoneNetwork;
import rzk.wirelessredstone.client.LangKeys;
import rzk.wirelessredstone.client.gui.GuiFrequency;
import rzk.wirelessredstone.integration.ProbeInfoProvider;
import rzk.wirelessredstone.tile.TileFrequency;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class BlockFrequency extends BlockRedstoneDevice implements ProbeInfoProvider
{
	public final boolean isTransmitter;

	public BlockFrequency(boolean isTransmitter)
	{
		super(Properties.create(Material.IRON).hardnessAndResistance(1.5F, 6.0F).sound(SoundType.METAL));
		this.isTransmitter = isTransmitter;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
	{
		if (world.isRemote)
			WorldUtils.ifTilePresent(world, pos, TileFrequency.class, tile ->
					DistExecutor.runWhenOn(Dist.CLIENT, () -> GuiFrequency.openGui(tile.getFrequency(), pos)));
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
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
	{
		return Collections.singletonList(new ItemStack(this));
	}

	@Override
	public void addProbeInfo(IProbeInfo info, World world, IProbeHitData data)
	{
		ObjectUtils.ifCastable(world.getTileEntity(data.getPos()), TileFrequency.class, tile ->
				info.horizontal().text(CompoundText.createLabelInfo(new TranslationTextComponent(LangKeys.Tooltip.FREQUENCY).getString() + ": ", tile.getFrequency())));
	}
}
