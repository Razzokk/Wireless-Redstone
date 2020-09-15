package rzk.wirelessredstone.packet;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;
import rzk.lib.mc.packet.Packet;
import rzk.lib.util.ObjectUtils;
import rzk.wirelessredstone.item.ItemFrequency;

import java.util.function.Supplier;

public class PacketFrequencyItem extends PacketFrequency
{
	private final Hand hand;

	public PacketFrequencyItem(Hand hand)
	{
		super(-1);
		this.hand = hand;
	}

	public PacketFrequencyItem(PacketBuffer buffer)
	{
		super(buffer);
		hand = buffer.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
	}

	@Override
	public void toBytes(PacketBuffer buffer)
	{
		super.toBytes(buffer);
		buffer.writeBoolean(hand == Hand.MAIN_HAND);
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			ServerPlayerEntity player = ctx.get().getSender();
			ItemStack stack = player != null ? player.getHeldItem(hand) : ItemStack.EMPTY;

			if (!stack.isEmpty() && stack.getItem() instanceof ItemFrequency)
				ObjectUtils.ifCastable(stack.getItem(), ItemFrequency.class, item -> item.setFrequency(stack, getFrequency()));

		});
		ctx.get().setPacketHandled(true);
	}
}
