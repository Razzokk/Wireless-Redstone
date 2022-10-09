package rzk.wirelessredstone.registry;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.blockentity.RedstoneReceiverBlockEntity;
import rzk.wirelessredstone.blockentity.RedstoneTransmitterBlockEntity;

import java.util.function.Supplier;

public class ModBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, WirelessRedstone.MODID);

	public static final RegistryObject<BlockEntityType<RedstoneTransmitterBlockEntity>> REDSTONE_TRANSMITTER_BLOCK_ENTITY_TYPE =
			registerBlockEntity("redstone_transmitter", () ->
					BlockEntityType.Builder.of(RedstoneTransmitterBlockEntity::new, ModBlocks.REDSTONE_TRANSMITTER.get()).build(null));

	public static final RegistryObject<BlockEntityType<RedstoneReceiverBlockEntity>> REDSTONE_RECEIVER_BLOCK_ENTITY_TYPE =
			registerBlockEntity("redstone_receiver", () ->
					BlockEntityType.Builder.of(RedstoneReceiverBlockEntity::new, ModBlocks.REDSTONE_RECEIVER.get()).build(null));

	public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntity(String name, Supplier<BlockEntityType<T>> supplier)
	{
		return BLOCK_ENTITY_TYPES.register(name, supplier);
	}
}
