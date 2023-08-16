package rzk.wirelessredstone.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import rzk.wirelessredstone.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider
{
	public BlockTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture)
	{
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup arg)
	{
		getTagBuilder(BlockTags.PICKAXE_MINEABLE)
			.add(getId(ModBlocks.REDSTONE_TRANSMITTER))
			.add(getId(ModBlocks.REDSTONE_RECEIVER));
	}

	private static Identifier getId(Block block)
	{
		return Registries.BLOCK.getId(block);
	}

	private static Identifier getId(Item item)
	{
		return Registries.ITEM.getId(item);
	}
}
