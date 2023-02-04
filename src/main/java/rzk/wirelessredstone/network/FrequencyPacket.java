package rzk.wirelessredstone.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

public abstract class FrequencyPacket
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

	public PacketByteBuf toPacketByteBuf()
	{
		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeInt(frequency);
		writeAdditional(buf);
		return buf;
	}

	public abstract void writeAdditional(PacketByteBuf buf);
}
