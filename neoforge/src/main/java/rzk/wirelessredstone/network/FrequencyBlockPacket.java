package rzk.wirelessredstone.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.client.screen.ModScreens;
import rzk.wirelessredstone.misc.TranslationKeys;

public record FrequencyBlockPacket(int frequency, BlockPos pos) implements CustomPayload
{
	public static final Identifier ID = WirelessRedstone.identifier("frequency_block");

	public FrequencyBlockPacket(PacketByteBuf buf)
	{
		this(buf.readInt(), buf.readBlockPos());
	}

	@Override
	public void write(PacketByteBuf buf)
	{
		buf.writeInt(frequency);
		buf.writeBlockPos(pos);
	}

	@Override
	public Identifier id()
	{
		return ID;
	}

	public void handleServer(IPayloadContext ctx)
	{
		ctx.workHandler().submitAsync(() -> {
			World world = ctx.level().orElseThrow();
			if (world.isAreaLoaded(pos, 0) && world.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock block)
				block.setFrequency(world, pos, frequency);
		}).exceptionally(e -> {
			ctx.packetHandler().disconnect(Text.translatable(TranslationKeys.NETWORKING_FAILED, e.getMessage()));
			return null;
		});
	}

	public void handleClient(IPayloadContext ctx)
	{
		ctx.workHandler().submitAsync(() -> {
			if (FMLEnvironment.dist == Dist.CLIENT)
				ModScreens.openBlockFrequencyScreen(frequency, pos);
		}).exceptionally(e -> {
			ctx.packetHandler().disconnect(Text.translatable(TranslationKeys.NETWORKING_FAILED, e.getMessage()));
			return null;
		});
	}
}
