package rzk.wirelessredstone.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.ModBlocks;

public final class ModBlockEntitiesForge
{
	private ModBlockEntitiesForge() {}

	public static void registerBlockEntities(RegisterEvent event)
	{
		event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, helper ->
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
