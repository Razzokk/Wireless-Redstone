package rzk.wirelessredstone.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import rzk.wirelessredstone.item.FrequencyItem;

import java.util.function.Supplier;

public class FrequencyItemPacket extends FrequencyPacket
{
	private final InteractionHand hand;

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
	public void encodeAdditional(FriendlyByteBuf buf)
	{
		buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			ServerPlayer player = ctx.get().getSender();
			ItemStack stack = player.getItemInHand(hand);

			if (stack.getItem() instanceof FrequencyItem item)
				item.setFrequency(stack, frequency);
		});
		ctx.get().setPacketHandled(true);
	}
}
