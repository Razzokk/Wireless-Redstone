package rzk.wirelessredstone.generators;

import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;
import rzk.wirelessredstone.registries.ModBlocks;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModLootTables extends LootTableProvider
{
    public ModLootTables(DataGenerator generator)
    {
        super(generator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables()
    {
        return List.of(Pair.of(ModBlockLoot::new, LootContextParamSets.BLOCK));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker)
    {
        for (Map.Entry<ResourceLocation, LootTable> entry : map.entrySet())
            LootTables.validate(validationTracker, entry.getKey(), entry.getValue());
    }

    public static class ModBlockLoot extends BlockLoot
    {
        @Override
        protected void addTables()
        {
            dropSelf(ModBlocks.REDSTONE_TRANSMITTER.get());
            dropSelf(ModBlocks.REDSTONE_RECEIVER.get());
            dropSelf(ModBlocks.P2P_REDSTONE_TRANSMITTER.get());
            dropSelf(ModBlocks.P2P_REDSTONE_RECEIVER.get());
            dropSelf(ModBlocks.MODEL_TEST.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks()
        {
            return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).toList();
        }
    }
}
