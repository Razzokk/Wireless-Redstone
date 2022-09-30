package rzk.wirelessredstone.registries;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.blockentities.P2PTransmitterBlockEntity;
import rzk.wirelessredstone.blockentities.RedstoneReceiverBlockEntity;
import rzk.wirelessredstone.blockentities.RedstoneTransmitterBlockEntity;

public class ModBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, WirelessRedstone.MODID);

	public static final RegistryObject<BlockEntityType<?>> REDSTONE_TRANSMITTER_BLOCK_ENTITY_TYPE =
			BLOCK_ENTITIES.register("redstone_transmitter", () ->
					BlockEntityType.Builder.of(RedstoneTransmitterBlockEntity::new, ModBlocks.REDSTONE_TRANSMITTER.get()).build(null));

	public static final RegistryObject<BlockEntityType<?>> REDSTONE_RECEIVER_BLOCK_ENTITY_TYPE =
			BLOCK_ENTITIES.register("redstone_receiver", () ->
					BlockEntityType.Builder.of(RedstoneReceiverBlockEntity::new, ModBlocks.REDSTONE_RECEIVER.get()).build(null));

	public static final RegistryObject<BlockEntityType<?>> SIMPLE_TRANSMITTER_BLOCK_ENTITY_TYPE =
			BLOCK_ENTITIES.register("simple_transmitter", () ->
					BlockEntityType.Builder.of(P2PTransmitterBlockEntity::new, ModBlocks.P2P_TRANSMITTER.get()).build(null));
}
