package rzk.wirelessredstone.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import rzk.wirelessredstone.client.WRClientEventsForge;

public record SnifferHighlightPacket(long timestamp, Hand hand, BlockPos[] coords)
{
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

	public void write(PacketByteBuf buf)
	{
		buf.writeLong(timestamp);
		buf.writeBoolean(hand == Hand.MAIN_HAND);
		buf.writeInt(coords.length);

		for (BlockPos pos : coords)
			buf.writeBlockPos(pos);
	}

	public void handle(CustomPayloadEvent.Context ctx)
	{
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> WRClientEventsForge.handleSnifferHighlightPacket(ctx, timestamp, hand, coords));
	}
}
