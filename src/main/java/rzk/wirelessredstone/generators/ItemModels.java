package rzk.wirelessredstone.generators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import rzk.wirelessredstone.registries.ModItems;

public final class ItemModels extends ItemModelProvider
{
    public ItemModels(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper)
    {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        basicItem(ModItems.FREQUENCY_TOOL.get());
        basicItem(ModItems.P2P_LINKER.get());
    }
}
