package rzk.wirelessredstone.packet;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import rzk.lib.mc.packet.Packet;
import rzk.lib.mc.util.Utils;
import rzk.lib.mc.util.WorldUtils;
import rzk.wirelessredstone.tile.TileFrequency;

import java.util.function.Supplier;

public class PacketFrequencyBlock extends Packet
{
	private int frequency;
	private BlockPos pos;

	public PacketFrequencyBlock(int frequency, BlockPos pos)
	{
		this.frequency = frequency;
		this.pos = pos;
	}

	PacketFrequencyBlock(PacketBuffer buffer)
	{
		super(buffer);
		frequency = buffer.readInt();
		pos = BlockPos.fromLong(buffer.readLong());
	}

	@Override
	public void toBytes(PacketBuffer buffer)
	{
		buffer.writeInt(frequency);
		buffer.writeLong(pos.toLong());
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			ServerPlayerEntity player = ctx.get().getSender();
			ServerWorld world;
			if (player != null && (world = player.getServerWorld()).isBlockLoaded(pos))
				WorldUtils.ifTilePresent(world, pos, TileFrequency.class, tile -> tile.setFrequency(frequency));

		});
		ctx.get().setPacketHandled(true);
	}
}
