package rzk.wirelessredstone.generators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.generators.language.LanguageDE;
import rzk.wirelessredstone.generators.language.LanguageEN;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(true, new BlockStates(generator, WirelessRedstone.MODID, existingFileHelper));
        generator.addProvider(true, new ItemModels(generator, WirelessRedstone.MODID, existingFileHelper));
        generator.addProvider(true, new Recipes(generator));
        generator.addProvider(true, new ModLootTables(generator));

        generator.addProvider(true, new LanguageEN(generator, WirelessRedstone.MODID));
        generator.addProvider(true, new LanguageDE(generator, WirelessRedstone.MODID));
    }
}
