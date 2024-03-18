package rzk.wirelessredstone.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import rzk.wirelessredstone.WirelessRedstone;

public record FrequencyItemPacket(int frequency, Hand hand) implements FabricPacket
{
	public static final PacketType<FrequencyItemPacket> TYPE = PacketType.create(
		WirelessRedstone.identifier("frequency_item"),
		FrequencyItemPacket::new);

	public FrequencyItemPacket(PacketByteBuf buf)
	{
		this(buf.readInt(), buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND);
	}

	@Override
	public void write(PacketByteBuf buf)
	{
		buf.writeInt(frequency);
		buf.writeBoolean(hand == Hand.MAIN_HAND);
	}

	@Override
	public PacketType<?> getType()
	{
		return TYPE;
	}
}
