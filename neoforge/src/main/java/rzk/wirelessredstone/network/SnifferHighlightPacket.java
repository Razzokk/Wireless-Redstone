package rzk.wirelessredstone.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.client.WRClientEventsNeo;
import rzk.wirelessredstone.misc.TranslationKeys;

public record SnifferHighlightPacket(long timestamp, Hand hand, BlockPos[] coords) implements CustomPayload
{
	public static final Identifier ID = WirelessRedstone.identifier("sniffer_highlight");

	public SnifferHighlightPacket(PacketByteBuf buf)
	{
		this(buf.readLong(), buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND, readCoords(buf));
	}

	private static BlockPos[] readCoords(PacketByteBuf buf)
	{
		var coords = new BlockPos[buf.readInt()];
		for (int i = 0; i < coords.length; i++) coords[i] = buf.readBlockPos();
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
	public Identifier id()
	{
		return ID;
	}

	public void handle(IPayloadContext ctx)
	{
		ctx.workHandler().submitAsync(() -> {
			if (FMLEnvironment.dist == Dist.CLIENT)
				WRClientEventsNeo.handleSnifferHighlightPacket(timestamp, hand, coords);
		}).exceptionally(e -> {
			ctx.packetHandler().disconnect(Text.translatable(TranslationKeys.NETWORKING_FAILED, e.getMessage()));
			return null;
		});
	}
}
