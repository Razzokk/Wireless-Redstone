package rzk.wirelessredstone.generator;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        // Client

        generator.addProvider(event.includeClient(), new BlockStates(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new ItemModels(output, existingFileHelper));
        generator.addProvider(event.includeClient(), new LanguageEN(output));
        generator.addProvider(event.includeClient(), new LanguageDE(output));

        // Server

        generator.addProvider(event.includeServer(), new Recipes(output));
        generator.addProvider(event.includeServer(), new ModLootTables(output));
        generator.addProvider(event.includeServer(), new ModBlockTags(output, event.getLookupProvider(), existingFileHelper));
    }
}
