package rzk.wirelessredstone.generator;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import rzk.wirelessredstone.registry.ModItems;

public final class ItemModels extends ItemModelProvider
{
    public ItemModels(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper)
    {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels()
    {
        basicItem(ModItems.CIRCUIT.get());
        basicItem(ModItems.FREQUENCY_TOOL.get());
        basicItem(ModItems.FREQUENCY_SNIFFER.get());
//        basicItem(ModItems.P2P_LINKER.get());
    }
}
