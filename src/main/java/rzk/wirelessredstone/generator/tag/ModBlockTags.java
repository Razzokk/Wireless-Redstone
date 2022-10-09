package rzk.wirelessredstone.generator.tag;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.registry.ModBlocks;

public class ModBlockTags extends BlockTagsProvider
{
	public ModBlockTags(DataGenerator gen, String modId, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(gen, modId, existingFileHelper);
	}

	@Override
	protected void addTags()
	{
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.REDSTONE_TRANSMITTER.get(), ModBlocks.REDSTONE_RECEIVER.get());
	}
}
