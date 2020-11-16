package rzk.wirelessredstone.datagen;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider
{
	public Recipes(DataGenerator generator)
	{
		super(generator);
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
	{
		ShapedRecipeBuilder.shapedRecipe(ModItems.CIRCUIT.get(), 4)
				.patternLine("RGR")
				.patternLine("IEI")
				.patternLine("QGQ")
				.key('R', Items.REDSTONE)
				.key('G', Items.GLOWSTONE_DUST)
				.key('I', Items.GOLD_INGOT)
				.key('E', Items.ENDER_PEARL)
				.key('Q', Items.QUARTZ)
				.addCriterion("item", InventoryChangeTrigger.Instance.forItems(Items.REDSTONE, Items.GLOWSTONE_DUST, Items.GOLD_INGOT, Items.ENDER_PEARL, Items.QUARTZ))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.TRANSMITTER.get())
				.patternLine("IRI")
				.patternLine("RCR")
				.patternLine("IRI")
				.key('I', Items.IRON_INGOT)
				.key('R', Items.REDSTONE_TORCH)
				.key('C', ModItems.CIRCUIT.get())
				.addCriterion("item", InventoryChangeTrigger.Instance.forItems(ModItems.CIRCUIT.get()))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModBlocks.RECEIVER.get())
				.patternLine("IRI")
				.patternLine("RCR")
				.patternLine("IRI")
				.key('I', Items.IRON_INGOT)
				.key('R', Items.REDSTONE)
				.key('C', ModItems.CIRCUIT.get())
				.addCriterion("item", InventoryChangeTrigger.Instance.forItems(ModItems.CIRCUIT.get()))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItems.REMOTE.get())
				.patternLine("TET")
				.patternLine("ICI")
				.patternLine("IRI")
				.key('T', Items.REDSTONE_TORCH)
				.key('E', Items.ENDER_PEARL)
				.key('I', Items.IRON_INGOT)
				.key('C', ModItems.CIRCUIT.get())
				.key('R', Items.REDSTONE)
				.addCriterion("item", InventoryChangeTrigger.Instance.forItems(ModItems.CIRCUIT.get()))
				.build(consumer);

		ShapedRecipeBuilder.shapedRecipe(ModItems.FREQUENCY_COPIER.get())
				.patternLine("RTR")
				.patternLine("ICI")
				.patternLine(" I ")
				.key('R', Items.REDSTONE)
				.key('T', Items.COMPARATOR)
				.key('I', Items.IRON_INGOT)
				.key('C', ModItems.CIRCUIT.get())
				.addCriterion("item", InventoryChangeTrigger.Instance.forItems(ModItems.CIRCUIT.get()))
				.build(consumer);
	}
}
