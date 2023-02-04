package rzk.wirelessredstone.generator;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;
import rzk.wirelessredstone.registry.ModBlocks;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ModLootTables extends LootTableProvider
{
	public ModLootTables(PackOutput packOutput)
	{
		super(packOutput, Collections.emptySet(), List.of(new LootTableProvider.SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK)));
	}

	@Override
	public List<SubProviderEntry> getTables()
	{
		return super.getTables();
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationTracker)
	{
		for (Map.Entry<ResourceLocation, LootTable> entry : map.entrySet())
			LootTables.validate(validationTracker, entry.getKey(), entry.getValue());
	}

	public static class ModBlockLoot extends BlockLootSubProvider
	{
		protected ModBlockLoot()
		{
			super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
		}

		@Override
		protected void generate()
		{
			dropSelf(ModBlocks.REDSTONE_TRANSMITTER.get());
			dropSelf(ModBlocks.REDSTONE_RECEIVER.get());
//            dropSelf(ModBlocks.P2P_REDSTONE_TRANSMITTER.get());
//            dropSelf(ModBlocks.P2P_REDSTONE_RECEIVER.get());
//            dropSelf(ModBlocks.MODEL_TEST.get());
		}

		@Override
		protected Iterable<Block> getKnownBlocks()
		{
			return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).toList();
		}
	}
}
