package rzk.wirelessredstone.packet;

import net.minecraft.network.PacketBuffer;
import rzk.lib.mc.packet.Packet;

public abstract class PacketFrequency extends Packet
{
	private int frequency;

	public PacketFrequency(int frequency)
	{
		this.frequency = frequency;
	}

	public PacketFrequency(PacketBuffer buffer)
	{
		super(buffer);
		frequency = buffer.readInt();
	}

	@Override
	public void toBytes(PacketBuffer buffer)
	{
		buffer.writeInt(frequency);
	}

	public void setFrequency(int frequency)
	{
		this.frequency = frequency;
	}

	public int getFrequency()
	{
		return frequency;
	}
}
