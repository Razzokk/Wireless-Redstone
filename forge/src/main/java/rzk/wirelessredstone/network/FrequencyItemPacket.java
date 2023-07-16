package rzk.wirelessredstone.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.item.FrequencyItem;

import java.util.function.Supplier;

public abstract class FrequencyItemPacket extends FrequencyPacket
{
	public final InteractionHand hand;

	public FrequencyItemPacket(int frequency, InteractionHand hand)
	{
		super(frequency);
		this.hand = hand;
	}

	public FrequencyItemPacket(FriendlyByteBuf buf)
	{
		super(buf);
		hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
	}

	@Override
	public void writeAdditional(FriendlyByteBuf buf)
	{
		buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
	}

	public static class SetFrequency extends FrequencyItemPacket
	{
		public SetFrequency(int frequency, InteractionHand hand)
		{
			super(frequency, hand);
		}

		public SetFrequency(FriendlyByteBuf buf)
		{
			super(buf);
		}

		public void handle(Supplier<NetworkEvent.Context> ctx)
		{
			ServerPlayer player = ctx.get().getSender();
			ItemStack stack = player.getItemInHand(hand);
			if (stack.getItem() instanceof FrequencyItem item)
				item.setFrequency(stack, frequency);
		}
	}

	public static class OpenScreen extends FrequencyItemPacket
	{
		public OpenScreen(int frequency, InteractionHand hand)
		{
			super(frequency, hand);
		}

		public OpenScreen(FriendlyByteBuf buf)
		{
			super(buf);
		}

		public void handle(Supplier<NetworkEvent.Context> ctx)
		{
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModScreens.openItemFrequencyScreen(frequency, hand));
		}
	}
}
