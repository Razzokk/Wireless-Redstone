package rzk.wirelessredstone.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.client.screen.ModScreens;

import java.util.function.Supplier;

public abstract class FrequencyBlockPacket extends FrequencyPacket
{
	public final BlockPos pos;

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
	public void writeAdditional(FriendlyByteBuf buf)
	{
		buf.writeBlockPos(pos);
	}

	public static class SetFrequency extends FrequencyBlockPacket
	{
		public SetFrequency(int frequency, BlockPos pos)
		{
			super(frequency, pos);
		}

		public SetFrequency(FriendlyByteBuf buf)
		{
			super(buf);
		}

		public void handle(Supplier<NetworkEvent.Context> ctx)
		{
			Level level = ctx.get().getSender().level;
			if (level.isLoaded(pos) && level.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock block)
				block.setFrequency(level, pos, frequency);
		}
	}

	public static class OpenScreen extends FrequencyBlockPacket
	{
		public OpenScreen(int frequency, BlockPos pos)
		{
			super(frequency, pos);
		}

		public OpenScreen(FriendlyByteBuf buf)
		{
			super(buf);
		}

		public void handle(Supplier<NetworkEvent.Context> ctx)
		{
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModScreens.openBlockFrequencyScreen(frequency, pos));
		}
	}
}
