package rzk.wirelessredstone.generator;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

import java.util.function.Consumer;

public final class Recipes extends RecipeProvider
{
    public Recipes(PackOutput packOutput)
    {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer)
    {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.CIRCUIT.get(), 2)
                .pattern("RGR")
                .pattern("IEI")
                .pattern("QGQ")
                .define('R', Items.REDSTONE)
                .define('G', Items.GLOWSTONE_DUST)
                .define('I', Items.GOLD_INGOT)
                .define('E', Items.ENDER_PEARL)
                .define('Q', Items.QUARTZ)
                .unlockedBy("item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.REDSTONE, Items.GLOWSTONE_DUST, Items.GOLD_INGOT, Items.ENDER_PEARL, Items.QUARTZ))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.REDSTONE_TRANSMITTER.get())
                .pattern("IRI")
                .pattern("RCR")
                .pattern("IRI")
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE_TORCH)
                .define('C', ModItems.CIRCUIT.get())
                .unlockedBy("item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CIRCUIT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.REDSTONE_RECEIVER.get())
                .pattern("IRI")
                .pattern("RCR")
                .pattern("IRI")
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('C', ModItems.CIRCUIT.get())
                .unlockedBy("item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CIRCUIT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.FREQUENCY_TOOL.get())
                .pattern("RTR")
                .pattern("ICI")
                .pattern(" I ")
                .define('R', Items.REDSTONE)
                .define('T', Items.COMPARATOR)
                .define('I', Items.IRON_INGOT)
                .define('C', ModItems.CIRCUIT.get())
                .unlockedBy("item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CIRCUIT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.FREQUENCY_SNIFFER.get())
                .pattern("ITI")
                .pattern("ICI")
                .pattern("ITI")
                .define('T', Items.COMPARATOR)
                .define('I', Items.IRON_INGOT)
                .define('C', ModItems.CIRCUIT.get())
                .unlockedBy("item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CIRCUIT.get()))
                .save(consumer);
    }
}
