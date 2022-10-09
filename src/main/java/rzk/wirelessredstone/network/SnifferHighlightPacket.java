package rzk.wirelessredstone.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SnifferHighlightPacket implements IPacket
{
	public final long timestamp;
	public final InteractionHand hand;
	public final BlockPos[] coords;

	public SnifferHighlightPacket(long timestamp, InteractionHand hand, BlockPos[] coords)
	{
		this.timestamp = timestamp;
		this.hand = hand;
		this.coords = coords;
	}

	public SnifferHighlightPacket(FriendlyByteBuf buf)
	{
		timestamp = buf.readLong();
		hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
		coords = new BlockPos[buf.readInt()];

		for (int i = 0; i < coords.length; i++)
			coords[i] = buf.readBlockPos();
	}

	@Override
	public void encode(FriendlyByteBuf buf)
	{
		buf.writeLong(timestamp);
		buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
		buf.writeInt(coords.length);

		for (BlockPos pos : coords)
			buf.writeBlockPos(pos);
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() -> SnifferHighlightHandle.handle(this));
		ctx.get().setPacketHandled(true);
	}
}
