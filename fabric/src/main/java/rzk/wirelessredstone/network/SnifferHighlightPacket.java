package rzk.wirelessredstone.network;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.WirelessRedstone;

public class SnifferHighlightPacket implements FabricPacket
{
	public static final PacketType<SnifferHighlightPacket> TYPE = PacketType.create(
		WirelessRedstone.identifier("networking/sniffer_highlight_packet"),
		SnifferHighlightPacket::new);

	public final long timestamp;
	public final Hand hand;
	public final BlockPos[] coords;

	public SnifferHighlightPacket(long timestamp, Hand hand, BlockPos[] coords)
	{
		this.timestamp = timestamp;
		this.hand = hand;
		this.coords = coords;
	}

	public SnifferHighlightPacket(PacketByteBuf buf)
	{
		timestamp = buf.readLong();
		hand = buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
		coords = new BlockPos[buf.readInt()];

		for (int i = 0; i < coords.length; i++)
			coords[i] = buf.readBlockPos();
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
