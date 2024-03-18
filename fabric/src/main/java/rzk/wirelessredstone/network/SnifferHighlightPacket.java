package rzk.wirelessredstone.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.WirelessRedstone;

public record SnifferHighlightPacket(long timestamp, Hand hand, BlockPos[] coords) implements FabricPacket
{
	public static final PacketType<SnifferHighlightPacket> TYPE = PacketType.create(
		WirelessRedstone.identifier("sniffer_highlight"),
		SnifferHighlightPacket::new);

	public SnifferHighlightPacket(PacketByteBuf buf)
	{
		this(buf.readLong(), buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND, readCoords(buf));
	}

	private static BlockPos[] readCoords(PacketByteBuf buf)
	{
		var coords = new BlockPos[buf.readInt()];
		for (int i = 0; i < coords.length; i++)
			coords[i] = buf.readBlockPos();
		return coords;
	}

	@Override
	public void write(PacketByteBuf buf)
	{
		buf.writeLong(timestamp);
		buf.writeBoolean(hand == Hand.MAIN_HAND);
		buf.writeInt(coords.length);

		for (BlockPos pos : coords)
			buf.writeBlockPos(pos);
	}

	@Override
	public PacketType<?> getType()
	{
		return TYPE;
	}
}
