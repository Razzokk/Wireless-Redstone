package rzk.wirelessredstone.registry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import rzk.wirelessredstone.WirelessRedstone;

public class ModRecipes
{
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
		addShapedRecipe(new ItemStack(ModItems.circuit, 4),
				"RGR",
				"IEI",
				"QGQ",
				'R', Items.REDSTONE,
				'G', Items.GLOWSTONE_DUST,
				'I', Items.GOLD_INGOT,
				'E', Items.ENDER_PEARL,
				'Q', Items.QUARTZ);

		addShapedRecipe(new ItemStack(ModBlocks.redstoneTransmitter),
				"ITI",
				"TCT",
				"ITI",
				'I', Items.IRON_INGOT,
				'T', Blocks.REDSTONE_TORCH,
				'C', ModItems.circuit);

		addShapedRecipe(new ItemStack(ModBlocks.redstoneReceiver),
				"IRI",
				"RCR",
				"IRI",
				'I', Items.IRON_INGOT,
				'R', Items.REDSTONE,
				'C', ModItems.circuit);

		addShapedRecipe(new ItemStack(ModItems.frequencyTool),
				"RKR",
				"ICI",
				" I ",
				'R', Items.REDSTONE,
				'K', Items.COMPARATOR,
				'I', Items.IRON_INGOT,
				'C', ModItems.circuit);

		addShapedRecipe(new ItemStack(ModItems.remote),
				"TET",
				"ICI",
				"IRI",
				'T', Blocks.REDSTONE_TORCH,
				'E', Items.ENDER_PEARL,
				'I', Items.IRON_INGOT,
				'C', ModItems.circuit,
				'R', Items.REDSTONE);
	}

	public static void addShapelessRecipe(ItemStack output, Ingredient... ingredients)
	{
		GameRegistry.addShapelessRecipe(new ResourceLocation(WirelessRedstone.MOD_ID, "recipe_" + output.getItem().getRegistryName().getResourcePath()), null, output, ingredients);
	}

	public static void addShapedRecipe(ItemStack output, Object... params)
	{
		GameRegistry.addShapedRecipe(new ResourceLocation(WirelessRedstone.MOD_ID, "recipe_" + output.getItem().getRegistryName().getResourcePath()), null, output, params);
	}
}
