package rzk.wirelessredstone.network;

import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.WirelessRedstone;

public class FrequencyBlockPacket extends FrequencyPacket
{
	public static final PacketType<FrequencyBlockPacket> TYPE = PacketType.create(
		WirelessRedstone.identifier("networking/frequency_block_packet"),
		FrequencyBlockPacket::new);

	public final BlockPos pos;

	public FrequencyBlockPacket(int frequency, BlockPos pos)
	{
		super(frequency);
		this.pos = pos;
	}

	public FrequencyBlockPacket(PacketByteBuf buf)
	{
		super(buf);
		pos = buf.readBlockPos();
	}

	@Override
	public void writeAdditional(PacketByteBuf buf)
	{
		buf.writeBlockPos(pos);
	}

	@Override
	public PacketType<?> getType()
	{
		return TYPE;
	}
}
