package rzk.wirelessredstone.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.misc.TranslationKeys;

public record FrequencyItemPacket(int frequency, Hand hand) implements CustomPayload
{
	public static final Identifier ID = WirelessRedstone.identifier("frequency_item");

	public FrequencyItemPacket(PacketByteBuf buf)
	{
		this(buf.readInt(), buf.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND);
	}

	@Override
	public void write(PacketByteBuf buf)
	{
		buf.writeInt(frequency);
		buf.writeBoolean(hand == Hand.MAIN_HAND);
	}

	@Override
	public Identifier id()
	{
		return ID;
	}

	public void handleServer(IPayloadContext ctx)
	{
		ctx.workHandler().submitAsync(() -> {
			PlayerEntity player = ctx.player().orElseThrow();
			ItemStack stack = player.getStackInHand(hand);
			if (stack.getItem() instanceof FrequencyItem item)
				item.setFrequency(stack, frequency);
		}).exceptionally(e -> {
			ctx.packetHandler().disconnect(Text.translatable(TranslationKeys.NETWORKING_FAILED, e.getMessage()));
			return null;
		});
	}

	public void handleClient(IPayloadContext ctx)
	{
		ctx.workHandler().submitAsync(() -> {
			if (FMLEnvironment.dist == Dist.CLIENT)
				ModScreens.openItemFrequencyScreen(frequency, hand);
		}).exceptionally(e -> {
			ctx.packetHandler().disconnect(Text.translatable(TranslationKeys.NETWORKING_FAILED, e.getMessage()));
			return null;
		});
	}
}
