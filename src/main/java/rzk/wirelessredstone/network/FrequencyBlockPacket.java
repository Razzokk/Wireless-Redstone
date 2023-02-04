package rzk.wirelessredstone.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.WirelessRedstone;

public class FrequencyBlockPacket extends FrequencyPacket
{
	public static final Identifier ID = WirelessRedstone.identifier("networking/frequency_block_packet");

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
}
