package rzk.wirelessredstone.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import rzk.wirelessredstone.api.RedstoneConnectable;
import rzk.wirelessredstone.misc.WRConfig;

import static net.minecraft.state.property.Properties.POWERED;

public class P2pRedstoneReceiverBlock extends Block implements RedstoneConnectable
{
	public P2pRedstoneReceiverBlock()
	{
		super(AbstractBlock.Settings.create()
			.mapColor(MapColor.IRON_GRAY)
			.solidBlock((state, blockGetter, pos) -> false)
			.strength(1.5F, 5.0F)
			.sounds(BlockSoundGroup.METAL));

		setDefaultState(stateManager.getDefaultState().with(POWERED, false));
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction)
	{
		return state.get(POWERED) && connectsToRedstone(state, world, pos, direction) ?
			WRConfig.redstoneReceiverSignalStrength : 0;
	}

	@Override
	public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction)
	{
		return WRConfig.redstoneReceiverStrongPower ? getWeakRedstonePower(state, world, pos, direction) : 0;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(POWERED);
	}
}
