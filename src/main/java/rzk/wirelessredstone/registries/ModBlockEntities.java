package rzk.wirelessredstone.registries;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.blockentities.WirelessBlockEntity;

public class ModBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, WirelessRedstone.MODID);

	public static final RegistryObject<BlockEntityType<?>> WIRELESS_BLOCK_ENTITY =
			BLOCK_ENTITIES.register("wireless_entity", () ->
					BlockEntityType.Builder.of(WirelessBlockEntity::new, ModBlocks.RECEIVER.get(), ModBlocks.TRANSMITTER.get()).build(null));
}
