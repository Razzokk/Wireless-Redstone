package rzk.wirelessredstone.network.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class PacketType<T extends Packet>
{
	public final Identifier identifier;
	private final Function<PacketByteBuf, T> creator;

	private PacketType(Identifier identifier, Function<PacketByteBuf, T> creator)
	{
		this.identifier = identifier;
		this.creator = creator;
	}

	public static <T extends Packet> PacketType<T> create(Identifier identifier, Function<PacketByteBuf, T> creator)
	{
		return new PacketType<>(identifier, creator);
	}

	public T createPacket(PacketByteBuf buffer)
	{
		return creator.apply(buffer);
	}
}
