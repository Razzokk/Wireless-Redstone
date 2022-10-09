package rzk.wirelessredstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class ModelTest extends Block
{
    private static final VoxelShape BB_UP = Block.box(2, 0, 2, 14, 4, 14);
    private static final VoxelShape BB_DOWN = Block.box(2, 12, 2, 14, 16, 14);
    private static final VoxelShape BB_SOUTH = Block.box(2, 2, 0, 14, 14, 4);
    private static final VoxelShape BB_NORTH = Block.box(2, 2, 12, 14, 14, 16);
    private static final VoxelShape BB_EAST = Block.box(0, 2, 2, 4, 14, 14);
    private static final VoxelShape BB_WEST = Block.box(12, 2, 2, 16, 14, 14);

    public ModelTest(Properties props)
    {
        super(props);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext)
    {
        return switch (state.getValue(FACING))
                {
                    case UP -> BB_UP;
                    case DOWN -> BB_DOWN;
                    case NORTH -> BB_NORTH;
                    case SOUTH -> BB_SOUTH;
                    case EAST -> BB_EAST;
                    case WEST -> BB_WEST;
                };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext)
    {
        return defaultBlockState().setValue(FACING, placeContext.getClickedFace());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction from, BlockState fromState, LevelAccessor levelAccessor, BlockPos pos, BlockPos fromPos)
    {
        return fromState.isFaceSturdy(levelAccessor, fromPos, state.getValue(FACING), SupportType.CENTER) ? state : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos)
    {
        Direction facing = state.getValue(FACING);
        BlockPos offset = pos.relative(facing.getOpposite());
        return levelReader.getBlockState(offset).isFaceSturdy(levelReader, offset, facing, SupportType.CENTER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }
}
