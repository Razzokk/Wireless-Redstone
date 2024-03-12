package rzk.wirelessredstone.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.client.screen.ModScreens;

public abstract class FrequencyBlockPacket extends FrequencyPacket
{
	public final BlockPos pos;

	public FrequencyBlockPacket(int frequency, BlockPos pos)
	{
		super(frequency);
		this.pos = pos;
	}

	public FrequencyBlockPacket(PacketByteBuf buf)
	{
		super(buf);
		pos = buf.readBlockPos();
	}

	@Override
	public void writeAdditional(PacketByteBuf buf)
	{
		buf.writeBlockPos(pos);
	}

	public static class SetFrequency extends FrequencyBlockPacket
	{
		public SetFrequency(int frequency, BlockPos pos)
		{
			super(frequency, pos);
		}

		public SetFrequency(PacketByteBuf buf)
		{
			super(buf);
		}

		public void handle(CustomPayloadEvent.Context ctx)
		{
			World world = ctx.getSender().getWorld();
			if (world.isChunkLoaded(pos) && world.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock block)
				block.setFrequency(world, pos, frequency);
		}
	}

	public static class OpenScreen extends FrequencyBlockPacket
	{
		public OpenScreen(int frequency, BlockPos pos)
		{
			super(frequency, pos);
		}

		public OpenScreen(PacketByteBuf buf)
		{
			super(buf);
		}

		public void handle(CustomPayloadEvent.Context ctx)
		{
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModScreens.openBlockFrequencyScreen(frequency, pos));
		}
	}
}
