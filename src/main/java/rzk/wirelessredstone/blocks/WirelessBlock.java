package rzk.wirelessredstone.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.blockentities.WirelessBlockEntity;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class WirelessBlock extends Block implements EntityBlock
{
	public WirelessBlock(Properties props)
	{
		super(props);
		registerDefaultState(stateDefinition.any().setValue(POWERED, false));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
	{
		if (player.isShiftKeyDown() && level.getBlockEntity(pos) instanceof WirelessBlockEntity wireless)
		{
			int freq = wireless.getFreq();
			wireless.setFreq((freq + 1) % 16);
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	protected int getFreq(Level level, BlockPos pos)
	{
		if (level.getBlockEntity(pos) instanceof WirelessBlockEntity wireless)
			return wireless.getFreq();

		return 0;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new WirelessBlockEntity(pos, state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(POWERED);
	}
}
