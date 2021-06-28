package rzk.wirelessredstone.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface Packet
{
	void toBytes(PacketBuffer buffer);

	boolean handle(Supplier<NetworkEvent.Context> ctx);
}
