package rzk.wirelessredstone.datagen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import rzk.wirelessredstone.registry.ModBlocks;

import java.util.function.Function;

public class BlockModels extends BlockStateProvider
{
	public BlockModels(DataGenerator generator, String modid, ExistingFileHelper fileHelper)
	{
		super(generator, modid, fileHelper);
	}

	private static String name(Block block)
	{
		return block.getRegistryName().getPath();
	}

	@Override
	protected void registerStatesAndModels()
	{
		wirelessBlock(ModBlocks.TRANSMITTER.get());
		wirelessBlock(ModBlocks.RECEIVER.get());
	}

	private void wirelessBlock(Block block)
	{
		Function<BlockState, ModelFile> modelFunc = state ->
		{
			boolean powered = state.get(BlockStateProperties.POWERED);
			String name = name(block);
			String power = "_" + (powered ? "on" : "off");
			return models().cubeBottomTop(name + power,
					modLoc("blocks/" + name + "_side" + power),
					modLoc("blocks/" + name + "_bottom" + power),
					modLoc("blocks/" + name + "_top" + power));
		};

		getVariantBuilder(block)
				.forAllStates(state -> ConfiguredModel.builder()
						.modelFile(modelFunc.apply(state))
						.build());
	}
}
