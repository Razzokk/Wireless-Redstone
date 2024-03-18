package rzk.wirelessredstone.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.WirelessRedstone;

public record FrequencyBlockPacket(int frequency, BlockPos pos) implements FabricPacket
{
	public static final PacketType<FrequencyBlockPacket> TYPE = PacketType.create(
		WirelessRedstone.identifier("frequency_block"),
		FrequencyBlockPacket::new);

	public FrequencyBlockPacket(PacketByteBuf buf)
	{
		this(buf.readInt(), buf.readBlockPos());
	}

	@Override
	public void write(PacketByteBuf buf)
	{
		buf.writeInt(frequency);
		buf.writeBlockPos(pos);
	}

	@Override
	public PacketType<?> getType()
	{
		return TYPE;
	}
}
