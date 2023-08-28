package rzk.wirelessredstone.network;

import net.minecraft.network.PacketByteBuf;
import rzk.wirelessredstone.network.packet.Packet;

public abstract class FrequencyPacket implements Packet
{
	public final int frequency;

	public FrequencyPacket(int frequency)
	{
		this.frequency = frequency;
	}

	public FrequencyPacket(PacketByteBuf buf)
	{
		frequency = buf.readInt();
	}

	@Override
	public void write(PacketByteBuf buf)
	{
		buf.writeInt(frequency);
		writeAdditional(buf);
	}

	public abstract void writeAdditional(PacketByteBuf buf);
}
