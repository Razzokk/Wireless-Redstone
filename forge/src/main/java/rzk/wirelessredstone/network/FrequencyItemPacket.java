package rzk.wirelessredstone.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.item.FrequencyItem;

import java.util.function.Supplier;

public abstract class FrequencyItemPacket extends FrequencyPacket
{
	public final Hand hand;

	public FrequencyItemPacket(int frequency, Hand hand)
	{
		super(frequency);
		this.hand = hand;
	}

	public FrequencyItemPacket(PacketByteBuf buf)
	{
		super(buf);
		hand = buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
	}

	@Override
	public void writeAdditional(PacketByteBuf buf)
	{
		buf.writeBoolean(hand == Hand.MAIN_HAND);
	}

	public static class SetFrequency extends FrequencyItemPacket
	{
		public SetFrequency(int frequency, Hand hand)
		{
			super(frequency, hand);
		}

		public SetFrequency(PacketByteBuf buf)
		{
			super(buf);
		}

		public void handle(Supplier<NetworkEvent.Context> ctx)
		{
			ServerPlayerEntity player = ctx.get().getSender();
			ItemStack stack = player.getStackInHand(hand);
			if (stack.getItem() instanceof FrequencyItem item)
				item.setFrequency(stack, frequency);
		}
	}

	public static class OpenScreen extends FrequencyItemPacket
	{
		public OpenScreen(int frequency, Hand hand)
		{
			super(frequency, hand);
		}

		public OpenScreen(PacketByteBuf buf)
		{
			super(buf);
		}

		public void handle(Supplier<NetworkEvent.Context> ctx)
		{
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModScreens.openItemFrequencyScreen(frequency, hand));
		}
	}
}
