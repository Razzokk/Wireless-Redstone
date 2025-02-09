package rzk.wirelessredstone.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

public class RecipeGenerator extends FabricRecipeProvider
{
	public RecipeGenerator(FabricDataOutput output)
	{
		super(output);
	}

	@Override
	public void generate(RecipeExporter exporter)
	{
		ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModItems.circuit, 2)
			.pattern("RGR")
			.pattern("IEI")
			.pattern("QGQ")
			.input('R', Items.REDSTONE)
			.input('G', Items.GLOWSTONE_DUST)
			.input('I', Items.GOLD_INGOT)
			.input('E', Items.ENDER_PEARL)
			.input('Q', Items.QUARTZ)
			.criterion(hasItem(Items.REDSTONE), conditionsFromItem(Items.REDSTONE))
			.criterion(hasItem(Items.GLOWSTONE_DUST), conditionsFromItem(Items.GLOWSTONE_DUST))
			.criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
			.criterion(hasItem(Items.ENDER_PEARL), conditionsFromItem(Items.ENDER_PEARL))
			.criterion(hasItem(Items.QUARTZ), conditionsFromItem(Items.QUARTZ))
			.offerTo(exporter);

		ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.redstoneTransmitter)
			.pattern("IRI")
			.pattern("RCR")
			.pattern("IRI")
			.input('I', Items.IRON_INGOT)
			.input('R', Items.REDSTONE_TORCH)
			.input('C', ModItems.circuit)
			.criterion(hasItem(ModItems.circuit), conditionsFromItem(ModItems.circuit))
			.offerTo(exporter);

		ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.redstoneReceiver)
			.pattern("IRI")
			.pattern("RCR")
			.pattern("IRI")
			.input('I', Items.IRON_INGOT)
			.input('R', Items.REDSTONE)
			.input('C', ModItems.circuit)
			.criterion(hasItem(ModItems.circuit), conditionsFromItem(ModItems.circuit))
			.offerTo(exporter);

		ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModItems.frequencyTool)
			.pattern("RTR")
			.pattern("ICI")
			.pattern(" I ")
			.input('R', Items.REDSTONE)
			.input('T', Items.COMPARATOR)
			.input('I', Items.IRON_INGOT)
			.input('C', ModItems.circuit)
			.criterion(hasItem(ModItems.circuit), conditionsFromItem(ModItems.circuit))
			.offerTo(exporter);

		ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModItems.frequencySniffer)
			.pattern("ITI")
			.pattern("ICI")
			.pattern("ITI")
			.input('T', Items.COMPARATOR)
			.input('I', Items.IRON_INGOT)
			.input('C', ModItems.circuit)
			.criterion(hasItem(ModItems.circuit), conditionsFromItem(ModItems.circuit))
			.offerTo(exporter);

		ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModItems.remote)
			.pattern("TPT")
			.pattern("ICI")
			.pattern("IRI")
			.input('T', Items.REDSTONE_TORCH)
			.input('P', Items.ENDER_PEARL)
			.input('I', Items.IRON_INGOT)
			.input('C', ModItems.circuit)
			.input('R', Items.REDSTONE)
			.criterion(hasItem(ModItems.circuit), conditionsFromItem(ModItems.circuit))
			.offerTo(exporter);
	}
}
