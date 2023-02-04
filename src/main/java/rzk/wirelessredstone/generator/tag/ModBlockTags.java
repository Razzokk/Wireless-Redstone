package rzk.wirelessredstone.generator.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.registry.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockTags extends BlockTagsProvider
{
	public ModBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, lookupProvider, WirelessRedstone.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider)
	{
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.REDSTONE_TRANSMITTER.get(), ModBlocks.REDSTONE_RECEIVER.get());
	}
}
