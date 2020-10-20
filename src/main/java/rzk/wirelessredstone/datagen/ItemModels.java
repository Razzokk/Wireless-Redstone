package rzk.wirelessredstone.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

public class ItemModels extends ItemModelProvider
{
	public ItemModels(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper)
	{
		super(generator, modid, existingFileHelper);
	}

	public static String name(Item item)
	{
		return item.getRegistryName().getPath();
	}

	@Override
	protected void registerModels()
	{
		simpleItem(ModItems.CIRCUIT);
		simpleItem(ModItems.FREQUENCY_COPIER);
		wirelessRemote(ModItems.REMOTE);
		wirelessBlock(ModBlocks.TRANSMITTER);
		wirelessBlock(ModBlocks.RECEIVER);
	}

	public void simpleItem(Item item)
	{
		String itemName = name(item);
		singleTexture(itemName, mcLoc("item/generated"), "layer0", modLoc("items/" + itemName));
	}

	public void wirelessRemote(Item item)
	{
		String itemName = name(item);
		singleTexture(itemName, mcLoc("item/generated"), "layer0", modLoc("items/" + itemName + "_off"))
				.override()
				.predicate(new ResourceLocation("powered"), 1)
				.model(singleTexture(itemName + "_on", mcLoc("item/generated"), "layer0", modLoc("items/" + itemName + "_on")))
				.end();
	}

	public void wirelessBlock(Block block)
	{
		Item item = block.asItem();
		withExistingParent(name(item), modLoc("block/" + name(item) + "_off"));
	}

	@Override
	public String getName()
	{
		return "WirelessRedstone Item Model Provider";
	}
}
