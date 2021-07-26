package rzk.wirelessredstone.network;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public interface Packet
{
	void toBytes(FriendlyByteBuf buffer);

	boolean handle(Supplier<NetworkEvent.Context> ctx);
}
