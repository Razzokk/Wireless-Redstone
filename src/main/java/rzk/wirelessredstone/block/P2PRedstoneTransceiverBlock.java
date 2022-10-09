package rzk.wirelessredstone.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class P2PRedstoneTransceiverBlock extends Block
{
    private static final BooleanProperty CONNECTED = BooleanProperty.create("connected");

    public P2PRedstoneTransceiverBlock(Properties props)
    {
        super(props);
        registerDefaultState(stateDefinition.any().setValue(POWERED, false).setValue(CONNECTED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(POWERED, CONNECTED);
    }
}
