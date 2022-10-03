package rzk.wirelessredstone.registries;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.blockentities.P2PRedstoneTransmitterBlockEntity;
import rzk.wirelessredstone.blockentities.RedstoneReceiverBlockEntity;
import rzk.wirelessredstone.blockentities.RedstoneTransmitterBlockEntity;

import java.util.function.Supplier;

public class ModBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, WirelessRedstone.MODID);

	public static final RegistryObject<BlockEntityType<?>> REDSTONE_TRANSMITTER_BLOCK_ENTITY_TYPE =
			registerBlockEntity("redstone_transmitter", () ->
					BlockEntityType.Builder.of(RedstoneTransmitterBlockEntity::new, ModBlocks.REDSTONE_TRANSMITTER.get()).build(null));

	public static final RegistryObject<BlockEntityType<?>> REDSTONE_RECEIVER_BLOCK_ENTITY_TYPE =
			registerBlockEntity("redstone_receiver", () ->
					BlockEntityType.Builder.of(RedstoneReceiverBlockEntity::new, ModBlocks.REDSTONE_RECEIVER.get()).build(null));

	public static final RegistryObject<BlockEntityType<?>> SIMPLE_TRANSMITTER_BLOCK_ENTITY_TYPE =
			registerBlockEntity("p2p_redstone_transmitter", () -> BlockEntityType.Builder.of(P2PRedstoneTransmitterBlockEntity::new, ModBlocks.P2P_REDSTONE_TRANSMITTER.get()).build(null));

	public static <T extends BlockEntity> RegistryObject<BlockEntityType<?>> registerBlockEntity(String name, Supplier<BlockEntityType<T>> supplier)
	{
		return BLOCK_ENTITIES.register(name, supplier);
	}
}
