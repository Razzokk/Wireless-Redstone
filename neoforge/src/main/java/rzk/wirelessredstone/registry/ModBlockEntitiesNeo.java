package rzk.wirelessredstone.registry;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.neoforged.neoforge.registries.RegisterEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.entity.RedstoneReceiverBlockEntity;
import rzk.wirelessredstone.block.entity.RedstoneTransmitterBlockEntity;

public final class ModBlockEntitiesNeo
{
	private ModBlockEntitiesNeo() {}

	public static void registerBlockEntities(RegisterEvent event)
	{
		event.register(Registries.BLOCK_ENTITY_TYPE.getKey(), helper ->
		{
			ModBlockEntities.redstoneTransmitterBlockEntityType = registerBlockEntityType(helper, "redstone_transmitter_block_entity", BlockEntityType.Builder.create(RedstoneTransmitterBlockEntity::new, ModBlocks.redstoneTransmitter).build(null));
			ModBlockEntities.redstoneReceiverBlockEntityType = registerBlockEntityType(helper, "redstone_receiver_block_entity", BlockEntityType.Builder.create(RedstoneReceiverBlockEntity::new, ModBlocks.redstoneReceiver).build(null));
		});
	}

	private static <T extends BlockEntity> BlockEntityType<T> registerBlockEntityType(RegisterEvent.RegisterHelper<BlockEntityType<?>> helper, String name, BlockEntityType<T> blockEntityType)
	{
		helper.register(WirelessRedstone.identifier(name), blockEntityType);
		return blockEntityType;
	}
}
