package rzk.wirelessredstone.network.packet;

import net.minecraft.network.PacketByteBuf;

public interface Packet
{
	void write(PacketByteBuf buffer);

	PacketType<?> getType();
}
