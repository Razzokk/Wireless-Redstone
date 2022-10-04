package rzk.wirelessredstone.network;

import net.minecraft.network.FriendlyByteBuf;

public abstract class FrequencyPacket implements IPacket
{
	protected final int frequency;

	public FrequencyPacket(int frequency)
	{
		this.frequency = frequency;
	}

	public FrequencyPacket(FriendlyByteBuf buf)
	{
		frequency = buf.readInt();
	}

	@Override
	public void encode(FriendlyByteBuf buf)
	{
		buf.writeInt(frequency);
		encodeAdditional(buf);
	}

	public abstract void encodeAdditional(FriendlyByteBuf buf);
}
