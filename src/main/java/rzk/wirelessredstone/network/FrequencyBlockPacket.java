package rzk.wirelessredstone.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import rzk.wirelessredstone.blocks.RedstoneTransceiverBlock;

import java.util.function.Supplier;

public class FrequencyBlockPacket extends FrequencyPacket
{
	private final BlockPos pos;

	public FrequencyBlockPacket(int frequency, BlockPos pos)
	{
		super(frequency);
		this.pos = pos;
	}

	public FrequencyBlockPacket(FriendlyByteBuf buf)
	{
		super(buf);
		pos = buf.readBlockPos();
	}

	@Override
	public void encodeAdditional(FriendlyByteBuf buf)
	{
		buf.writeBlockPos(pos);
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			ServerPlayer player = ctx.get().getSender();
			Level level = player.level;

			if (level.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock block)
				block.setFrequency(level, pos, frequency);
		});
		ctx.get().setPacketHandled(true);
	}
}
