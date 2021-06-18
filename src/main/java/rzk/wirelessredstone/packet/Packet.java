package rzk.wirelessredstone.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface Packet
{
	void toBytes(PacketBuffer buffer);

	void handle(Supplier<NetworkEvent.Context> ctx);
}
