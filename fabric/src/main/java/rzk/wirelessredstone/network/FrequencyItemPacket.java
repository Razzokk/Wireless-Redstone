package rzk.wirelessredstone.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.network.packet.PacketType;

public class FrequencyItemPacket extends FrequencyPacket
{
	public static final PacketType<FrequencyItemPacket> TYPE = PacketType.create(
		WirelessRedstone.identifier("networking/frequency_item_packet"),
		FrequencyItemPacket::new);

	public final Hand hand;

	public FrequencyItemPacket(int frequency, Hand hand)
	{
		super(frequency);
		this.hand = hand;
	}

	public FrequencyItemPacket(PacketByteBuf buf)
	{
		super(buf);
		hand = buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
	}

	@Override
	public void writeAdditional(PacketByteBuf buf)
	{
		buf.writeBoolean(hand == Hand.MAIN_HAND);
	}

	@Override
	public PacketType<?> getType()
	{
		return TYPE;
	}
}
