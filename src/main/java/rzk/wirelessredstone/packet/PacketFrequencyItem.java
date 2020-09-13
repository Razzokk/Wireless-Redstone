package rzk.wirelessredstone.packet;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;
import rzk.lib.mc.packet.Packet;
import rzk.wirelessredstone.item.ItemFrequency;

import java.util.function.Supplier;

public class PacketFrequencyItem extends Packet
{
	private int frequency;
	private Hand hand;

	public PacketFrequencyItem(int frequency, Hand hand)
	{
		this.frequency = frequency;
		this.hand = hand;
	}

	PacketFrequencyItem(PacketBuffer buffer)
	{
		super(buffer);
		frequency = buffer.readInt();
		hand = buffer.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
	}

	@Override
	public void toBytes(PacketBuffer buffer)
	{
		buffer.writeInt(frequency);
		buffer.writeBoolean(hand == Hand.MAIN_HAND);
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			ServerPlayerEntity player = ctx.get().getSender();
			ItemStack stack;
			if (player != null && (stack = player.getHeldItem(hand)).getItem() instanceof ItemFrequency)
			{
				CompoundNBT compound = new CompoundNBT();
				compound.putInt("frequency", frequency);
				stack.setTag(compound);
			}

		});
		ctx.get().setPacketHandled(true);
	}
}
