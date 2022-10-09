package rzk.wirelessredstone.generator;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.generator.language.LanguageDE;
import rzk.wirelessredstone.generator.language.LanguageEN;
import rzk.wirelessredstone.generator.tag.ModBlockTags;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataGenerators
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event)
    {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(new BlockStates(generator, WirelessRedstone.MODID, existingFileHelper));
        generator.addProvider(new ItemModels(generator, WirelessRedstone.MODID, existingFileHelper));
        generator.addProvider(new Recipes(generator));
        generator.addProvider(new ModLootTables(generator));

        generator.addProvider(new ModBlockTags(generator, WirelessRedstone.MODID, existingFileHelper));

        generator.addProvider(new LanguageEN(generator, WirelessRedstone.MODID));
        generator.addProvider(new LanguageDE(generator, WirelessRedstone.MODID));
    }
}
