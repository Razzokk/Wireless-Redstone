package rzk.wirelessredstone.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.item.FrequencyItem;

public record FrequencyItemPacket(int frequency, Hand hand)
{
	public FrequencyItemPacket(PacketByteBuf buf)
	{
		this(buf.readInt(), buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND);
	}

	public void write(PacketByteBuf buf)
	{
		buf.writeInt(frequency);
		buf.writeBoolean(hand == Hand.MAIN_HAND);
	}

	public void handle(CustomPayloadEvent.Context ctx)
	{
		if (ctx.isClientSide()) handleClient(ctx);
		else handleServer(ctx);
	}

	private void handleServer(CustomPayloadEvent.Context ctx)
	{
		ServerPlayerEntity player = ctx.getSender();
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getItem() instanceof FrequencyItem item)
			item.setFrequency(stack, frequency);
	}

	private void handleClient(CustomPayloadEvent.Context ctx)
	{
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ModScreens.openItemFrequencyScreen(frequency, hand));
	}
}
