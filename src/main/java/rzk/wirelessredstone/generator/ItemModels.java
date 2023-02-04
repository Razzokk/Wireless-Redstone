package rzk.wirelessredstone.generator;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.registry.ModItems;

public final class ItemModels extends ItemModelProvider
{
    public ItemModels(PackOutput output, ExistingFileHelper existingFileHelper)
    {
        super(output, WirelessRedstone.MODID, existingFileHelper);
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
