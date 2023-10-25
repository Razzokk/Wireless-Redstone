package rzk.wirelessredstone.network;

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

	public void write(PacketByteBuf buf)
	{
		buf.writeInt(frequency);
		writeAdditional(buf);
	}

	public abstract void writeAdditional(PacketByteBuf buf);
}
