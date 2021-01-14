package rzk.wirelessredstone.client.model;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

public class ModModels
{
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		registerBlockItem(ModBlocks.transmitter);
		registerBlockItem(ModBlocks.receiver);
		registerItem(ModItems.frequencyTool);
		registerItem(ModItems.frequencyTool);
	}

	public static void registerBlockItem(Block block)
	{
		registerItem(Item.getItemFromBlock(block));
	}

	public static void registerBlockItem(Block block, int meta)
	{
		registerItem(Item.getItemFromBlock(block), meta);
	}

	public static void registerBlockItem(Block block, int meta, ResourceLocation resourceLocation)
	{
		registerItem(Item.getItemFromBlock(block), meta, resourceLocation);
	}

	public static void registerBlockItem(Block block, int meta, ModelResourceLocation modelLocation)
	{
		registerItem(Item.getItemFromBlock(block), meta, modelLocation);
	}

	public static void registerItem(Item item)
	{
		registerItem(item, 0);
	}

	public static void registerItem(Item item, int meta)
	{
		registerItem(item, meta, item.getRegistryName());
	}

	public static void registerItem(Item item, int meta, ResourceLocation resourceLocation)
	{
		registerItem(item, meta, new ModelResourceLocation(resourceLocation, "inventory"));
	}

	public static void registerItem(Item item, int meta, ModelResourceLocation modelLocation)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, modelLocation);
	}
}
