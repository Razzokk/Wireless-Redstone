package rzk.wirelessredstone.packet;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import rzk.lib.mc.util.WorldUtils;
import rzk.wirelessredstone.tile.TileFrequency;

import java.util.function.Supplier;

public class PacketFrequencyBlock extends PacketFrequency
{
	private final BlockPos pos;

	public PacketFrequencyBlock(BlockPos pos)
	{
		super(-1);
		this.pos = pos;
	}

	public PacketFrequencyBlock(PacketBuffer buffer)
	{
		super(buffer);
		pos = BlockPos.fromLong(buffer.readLong());
	}

	@Override
	public void toBytes(PacketBuffer buffer)
	{
		super.toBytes(buffer);
		buffer.writeLong(pos.toLong());
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			ServerPlayerEntity player = ctx.get().getSender();
			ServerWorld world = player != null ? player.getServerWorld() : null;

			if (world != null && world.isAreaLoaded(pos, 0))
				WorldUtils.ifTilePresent(world, pos, TileFrequency.class, tile -> tile.setFrequency(getFrequency()));

		});
		ctx.get().setPacketHandled(true);
	}
}
