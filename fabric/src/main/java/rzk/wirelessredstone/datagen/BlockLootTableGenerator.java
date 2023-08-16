package rzk.wirelessredstone.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import rzk.wirelessredstone.block.ModBlocks;

public class BlockLootTableGenerator extends FabricBlockLootTableProvider
{
	protected BlockLootTableGenerator(FabricDataOutput dataOutput)
	{
		super(dataOutput);
	}

	@Override
	public void generate()
	{
		addDrop(ModBlocks.REDSTONE_TRANSMITTER);
		addDrop(ModBlocks.REDSTONE_RECEIVER);
	}
}
