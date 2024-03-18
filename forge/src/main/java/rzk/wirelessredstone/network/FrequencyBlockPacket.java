package rzk.wirelessredstone.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.client.screen.ModScreens;

public record FrequencyBlockPacket(int frequency, BlockPos pos)
{
	public FrequencyBlockPacket(PacketByteBuf buf)
	{
		this(buf.readInt(), buf.readBlockPos());
	}

	public void write(PacketByteBuf buf)
	{
		buf.writeInt(frequency);
		buf.writeBlockPos(pos);
	}

	public void handle(CustomPayloadEvent.Context ctx)
	{
		if (ctx.isClientSide()) handleClient(ctx);
		else handleServer(ctx);
	}

	private void handleServer(CustomPayloadEvent.Context ctx)
	{
		World world = ctx.getSender().getWorld();
		if (world.isChunkLoaded(pos) && world.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock block)
			block.setFrequency(world, pos, frequency);
	}

	private void handleClient(CustomPayloadEvent.Context ctx)
	{
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModScreens.openBlockFrequencyScreen(frequency, pos));
	}
}
