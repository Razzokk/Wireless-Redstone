package rzk.wirelessredstone.network;

import net.minecraft.network.FriendlyByteBuf;

public abstract class FrequencyPacket
{
	public final int frequency;

	public FrequencyPacket(int frequency)
	{
		this.frequency = frequency;
	}

	public FrequencyPacket(FriendlyByteBuf buf)
	{
		frequency = buf.readInt();
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeInt(frequency);
		writeAdditional(buf);
	}

	public abstract void writeAdditional(FriendlyByteBuf buf);
}
