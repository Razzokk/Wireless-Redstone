package rzk.wirelessredstone.generators;

import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import rzk.wirelessredstone.registries.ModBlocks;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public final class BlockStates extends BlockStateProvider
{
    public BlockStates(DataGenerator gen, String modid, ExistingFileHelper existingFileHelper)
    {
        super(gen, modid, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels()
    {
        onOffBlock("redstone_transmitter", ModBlocks.REDSTONE_TRANSMITTER.get());
        onOffBlock("redstone_receiver", ModBlocks.REDSTONE_RECEIVER.get());
        onOffBlock("p2p_transmitter", ModBlocks.P2P_TRANSMITTER.get());
        onOffBlock("p2p_receiver", ModBlocks.P2P_RECEIVER.get());
        modelTest("model_test", ModBlocks.MODEL_TEST.get());
    }

    private void onOffBlock(String name, Block block)
    {
        getVariantBuilder(block).forAllStates(state ->
        {
            String stateStr = state.getValue(POWERED) ? "on" : "off";

            return ConfiguredModel.builder()
                    .modelFile(models().cubeBottomTop(name + "_" + stateStr,
                        modLoc( "block/" + name + "_side_" + stateStr),
                        modLoc( "block/" + name + "_bottom_" + stateStr),
                        modLoc( "block/" + name + "_top_" + stateStr)))
                    .build();
        });
        simpleBlockItem(block, models().getExistingFile(modLoc(name + "_off")));
    }

    private void modelTest(String name, Block block)
    {
        ModelFile model = models()
                .withExistingParent(name, "minecraft:block/thin_block")
                .element()
                .from(2f,0f,2f)
                .to(14f,4f,14f)
                .allFaces((direction, faceBuilder) -> faceBuilder
                        .texture(
                            switch (direction)
                            {
                                case UP -> "#top";
                                case DOWN -> "#bottom";
                                default -> "#side";
                            })
                        .cullface(direction).end())
                .end()
                .texture("particle", modLoc("block/" + name + "_side"))
                .texture("top", modLoc("block/" + name + "_top"))
                .texture("bottom", modLoc("block/" + name + "_bottom"))
                .texture("side", modLoc("block/" + name + "_side"));

        getVariantBuilder(block).forAllStates(state ->
                {
                    Direction facing = state.getValue(FACING);

                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationX(facing == Direction.UP ? 0 : (facing == Direction.DOWN ? 180 : 90))
                            .rotationY((int) (facing == Direction.DOWN ? facing : facing.getOpposite()).toYRot())
                            .build();
                });
        simpleBlockItem(block, models().getExistingFile(modLoc(name)));
    }
}
