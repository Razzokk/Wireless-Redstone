package rzk.wirelessredstone.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.entity.RedstoneReceiverBlockEntity;
import rzk.wirelessredstone.block.entity.RedstoneTransmitterBlockEntity;

public final class ModBlockEntitiesFabric
{
	private ModBlockEntitiesFabric() {}

	public static void registerBlockEntities()
	{
		ModBlockEntities.redstoneTransmitterBlockEntityType = registerBlockEntity("redstone_transmitter_block_entity",
			FabricBlockEntityTypeBuilder.create(RedstoneTransmitterBlockEntity::new, ModBlocks.redstoneTransmitter).build());
		ModBlockEntities.redstoneReceiverBlockEntityType = registerBlockEntity("redstone_receiver_block_entity",
			FabricBlockEntityTypeBuilder.create(RedstoneReceiverBlockEntity::new, ModBlocks.redstoneReceiver).build());
	}

	private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, BlockEntityType<T> blockEntityType)
	{
		Registry.register(Registries.BLOCK_ENTITY_TYPE, WirelessRedstone.identifier(name), blockEntityType);
		return blockEntityType;
	}
}
