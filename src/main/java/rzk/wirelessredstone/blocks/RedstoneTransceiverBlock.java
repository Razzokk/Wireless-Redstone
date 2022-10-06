package rzk.wirelessredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import rzk.wirelessredstone.blockentities.RedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.client.screen.Screens;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public abstract class RedstoneTransceiverBlock extends Block implements EntityBlock
{
	public RedstoneTransceiverBlock(Properties props)
	{
		super(props.isRedstoneConductor((state, blockGetter, pos) -> false));
		registerDefaultState(stateDefinition.any().setValue(POWERED, false));
	}

	public void setFrequency(Level level, BlockPos pos, int frequency)
	{
		if (frequency != 0 && level.getBlockEntity(pos) instanceof RedstoneTransceiverBlockEntity transceiver)
			transceiver.setFrequency(frequency);
	}

	public int getFrequency(Level level, BlockPos pos)
	{
		if (level.getBlockEntity(pos) instanceof RedstoneTransceiverBlockEntity transceiver)
			return transceiver.getFrequency();
		return 0;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
	{
		if (level.isClientSide)
			Screens.openFrequencyBlockScreen(getFrequency(level, pos), pos);
		return InteractionResult.SUCCESS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(POWERED);
	}
}
