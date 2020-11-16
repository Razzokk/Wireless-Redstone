package rzk.wirelessredstone.packet;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;
import rzk.lib.mc.util.ObjectUtils;
import rzk.wirelessredstone.item.ItemFrequency;

import java.util.function.Supplier;

public class PacketFrequencyItem extends PacketFrequency
{
	private final Hand hand;

	public PacketFrequencyItem(int frequency, Hand hand)
	{
		super(frequency);
		this.hand = hand;
	}

	public PacketFrequencyItem(PacketBuffer buffer)
	{
		super(buffer);
		hand = buffer.readEnumValue(Hand.class);
	}

	@Override
	public void toBytes(PacketBuffer buffer)
	{
		super.toBytes(buffer);
		buffer.writeEnumValue(hand);
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			ServerPlayerEntity player = ctx.get().getSender();
			ItemStack stack = player != null ? player.getHeldItem(hand) : ItemStack.EMPTY;

			if (!stack.isEmpty() && stack.getItem() instanceof ItemFrequency)
				ObjectUtils.ifCastable(stack.getItem(), ItemFrequency.class, item -> item.setFrequency(player.getServerWorld(), stack, getFrequency()));
		});
		ctx.get().setPacketHandled(true);
	}
}
