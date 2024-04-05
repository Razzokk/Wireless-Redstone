package rzk.wirelessredstone.registry;

import net.minecraft.block.entity.BlockEntityType;
import rzk.wirelessredstone.block.entity.RedstoneReceiverBlockEntity;
import rzk.wirelessredstone.block.entity.RedstoneTransmitterBlockEntity;

public class ModBlockEntities
{
	public static BlockEntityType<RedstoneTransmitterBlockEntity> redstoneTransmitterBlockEntityType;
	public static BlockEntityType<RedstoneReceiverBlockEntity> redstoneReceiverBlockEntityType;

	private ModBlockEntities() {}
}
