package rzk.wirelessredstone.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import rzk.wirelessredstone.api.RedstoneConnectable;

import static net.minecraft.state.property.Properties.POWERED;
import static rzk.wirelessredstone.misc.WRProperties.LINKED;

public class P2pRedstoneTransceiverBlock extends Block implements RedstoneConnectable
{
	public P2pRedstoneTransceiverBlock()
	{
		super(AbstractBlock.Settings.create()
			.mapColor(MapColor.IRON_GRAY)
			.solidBlock((state, blockGetter, pos) -> false)
			.strength(1.5F, 5.0F)
			.sounds(BlockSoundGroup.METAL));

		setDefaultState(stateManager.getDefaultState()
			.with(LINKED, false)
			.with(POWERED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(LINKED, POWERED);
	}
}
