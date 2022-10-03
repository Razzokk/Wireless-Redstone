package rzk.wirelessredstone.generators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rzk.wirelessredstone.WirelessRedstone;

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
    }
}
