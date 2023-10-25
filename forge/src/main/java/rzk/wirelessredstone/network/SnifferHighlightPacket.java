package rzk.wirelessredstone.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import rzk.wirelessredstone.client.render.SnifferHighlightRenderer;

import java.util.function.Supplier;

public class SnifferHighlightPacket
{
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

	public void write(PacketByteBuf buf)
	{
		buf.writeLong(timestamp);
		buf.writeBoolean(hand == Hand.MAIN_HAND);
		buf.writeInt(coords.length);

		for (BlockPos pos : coords)
			buf.writeBlockPos(pos);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> SnifferHighlightRenderer.handleSnifferHighlightPacket(ctx, timestamp, hand, coords));
	}
}
