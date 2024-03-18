package rzk.wirelessredstone.platform;

import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.neoforged.neoforge.network.PacketDistributor;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;

public class ClientPlatformNeo implements ClientPlatform
{
	@Override
	public void sendFrequencyItemPacket(int frequency, Hand hand)
	{
		PacketDistributor.SERVER.noArg().send(new FrequencyItemPacket(frequency, hand));
	}

	@Override
	public void sendFrequencyBlockPacket(int frequency, BlockPos pos)
	{
		PacketDistributor.SERVER.noArg().send(new FrequencyBlockPacket(frequency, pos));
	}
}
