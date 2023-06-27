package rzk.wirelessredstone.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.client.VariantsBlockStateSupplier;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import rzk.wirelessredstone.block.ModBlocks;
import rzk.wirelessredstone.item.ModItems;

import java.util.function.Function;

public class ModelGenerator extends FabricModelProvider
{
	public ModelGenerator(FabricDataOutput output)
	{
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator generator)
	{
		transceiverBlock(generator, ModBlocks.REDSTONE_TRANSMITTER);
		transceiverBlock(generator, ModBlocks.REDSTONE_RECEIVER);
	}

	@Override
	public void generateItemModels(ItemModelGenerator generator)
	{
		generator.register(ModItems.CIRCUIT, Models.GENERATED);
		generator.register(ModItems.FREQUENCY_TOOL, Models.GENERATED);
		generator.register(ModItems.FREQUENCY_SNIFFER, Models.GENERATED);
	}

	private static void transceiverBlock(BlockStateModelGenerator generator, Block block)
	{
		Function<String, TextureMap> textureMap = state -> new TextureMap()
			.put(TextureKey.SIDE, TextureMap.getSubId(block, "_side_" + state))
			.put(TextureKey.TOP, TextureMap.getSubId(block, "_top_" + state))
			.put(TextureKey.BOTTOM, TextureMap.getSubId(block, "_bottom_" + state));

		Identifier off = Models.CUBE_BOTTOM_TOP.upload(ModelIds.getBlockSubModelId(block, "_off"), textureMap.apply("off"), generator.modelCollector);
		Identifier on = Models.CUBE_BOTTOM_TOP.upload(ModelIds.getBlockSubModelId(block, "_on"), textureMap.apply("on"), generator.modelCollector);

		generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
			.coordinate(BlockStateModelGenerator.createBooleanModelMap(Properties.POWERED, on, off)));
		generator.registerParentedItemModel(block, off);
	}
}
