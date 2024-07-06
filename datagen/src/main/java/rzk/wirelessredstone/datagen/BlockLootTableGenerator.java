package rzk.wirelessredstone.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import rzk.wirelessredstone.registry.ModBlocks;

public class BlockLootTableGenerator extends FabricBlockLootTableProvider
{
	protected BlockLootTableGenerator(FabricDataOutput dataOutput)
	{
		super(dataOutput);
	}

	@Override
	public void generate()
	{
		addDrop(ModBlocks.redstoneTransmitter);
		addDrop(ModBlocks.redstoneReceiver);
		addDrop(ModBlocks.p2pRedstoneTransmitter);
		addDrop(ModBlocks.p2pRedstoneReceiver);
	}
}
