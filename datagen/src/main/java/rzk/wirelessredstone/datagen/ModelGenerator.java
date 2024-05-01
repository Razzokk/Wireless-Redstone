package rzk.wirelessredstone.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

import java.util.function.BiFunction;
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
		transceiverBlock(generator, ModBlocks.redstoneTransmitter);
		transceiverBlock(generator, ModBlocks.redstoneReceiver);
	}

	@Override
	public void generateItemModels(ItemModelGenerator generator)
	{
		generator.register(ModItems.circuit, Models.GENERATED);
		generator.register(ModItems.frequencyTool, Models.GENERATED);
		generator.register(ModItems.frequencySniffer, Models.GENERATED);
		generator.register(ModItems.linker, Models.GENERATED);

		registerOverrides(generator, ModItems.remote, TextureMap.layer0(TextureMap.getSubId(ModItems.remote, "_off")),
			new ItemOverride("state", 1, (key, state) ->
				new Pair<>(ModelIds.getItemSubModelId(ModItems.remote, "_on"),
					TextureMap.layer0(TextureMap.getSubId(ModItems.remote, "_on")))));
	}

	private static void transceiverBlock(BlockStateModelGenerator generator, Block block)
	{
		Function<String, TextureMap> textureMap = state -> new TextureMap()
			.put(TextureKey.SIDE, TextureMap.getSubId(block, "/side_" + state))
			.put(TextureKey.TOP, TextureMap.getSubId(block, "/top_" + state))
			.put(TextureKey.BOTTOM, TextureMap.getSubId(block, "/bottom_" + state));

		Identifier off = Models.CUBE_BOTTOM_TOP.upload(ModelIds.getBlockSubModelId(block, "_off"), textureMap.apply("off"), generator.modelCollector);
		Identifier on = Models.CUBE_BOTTOM_TOP.upload(ModelIds.getBlockSubModelId(block, "_on"), textureMap.apply("on"), generator.modelCollector);

		generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block)
			.coordinate(BlockStateModelGenerator.createBooleanModelMap(Properties.POWERED, on, off)));
		generator.registerParentedItemModel(block, off);
	}

	private record ItemOverride(String key, float value,
								BiFunction<String, Float, Pair<Identifier, TextureMap>> modelTexturesProvider) {}

	private static void registerOverrides(ItemModelGenerator generator, Item item, TextureMap baseTextures, ItemOverride... overrides)
	{
		JsonArray array = new JsonArray();

		for (ItemOverride override : overrides)
		{
			JsonObject o = new JsonObject();
			JsonObject predicate = new JsonObject();
			predicate.addProperty(override.key, override.value);
			o.add("predicate", predicate);
			Pair<Identifier, TextureMap> modelTextures = override.modelTexturesProvider.apply(override.key, override.value);
			Identifier model = Models.GENERATED.upload(modelTextures.getLeft(), modelTextures.getRight(), generator.writer);
			o.addProperty("model", model.toString());
			array.add(o);
		}

		Models.GENERATED.upload(
			ModelIds.getItemModelId(item), baseTextures,
			generator.writer, (id, textures) ->
			{
				JsonObject jsonModel = Models.GENERATED.createJson(id, textures);
				jsonModel.add("overrides", array);
				return jsonModel;
			});
	}
}
