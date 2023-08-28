package rzk.wirelessredstone.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.ModBlocks;

public class ModBlockEntities
{
	public static final BlockEntityType<RedstoneTransmitterBlockEntity> REDSTONE_TRANSMITTER_BLOCK_ENTITY =
		FabricBlockEntityTypeBuilder.create(RedstoneTransmitterBlockEntity::new, ModBlocks.REDSTONE_TRANSMITTER).build();

	public static final BlockEntityType<RedstoneReceiverBlockEntity> REDSTONE_RECEIVER_BLOCK_ENTITY =
		FabricBlockEntityTypeBuilder.create(RedstoneReceiverBlockEntity::new, ModBlocks.REDSTONE_RECEIVER).build();

	public static void registerBlockEntities()
	{
		registerBlockEntity("redstone_transmitter_block_entity", REDSTONE_TRANSMITTER_BLOCK_ENTITY);
		registerBlockEntity("redstone_receiver_block_entity", REDSTONE_RECEIVER_BLOCK_ENTITY);
	}

	private static <T extends BlockEntity> void registerBlockEntity(String name, BlockEntityType<T> blockEntityType)
	{
		Registry.register(Registry.BLOCK_ENTITY_TYPE, WirelessRedstone.identifier(name), blockEntityType);
	}
}
