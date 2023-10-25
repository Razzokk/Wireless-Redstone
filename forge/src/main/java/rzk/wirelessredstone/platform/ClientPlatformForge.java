package rzk.wirelessredstone.platform;

import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.ModNetworking;

public class ClientPlatformForge implements ClientPlatform
{
	@Override
	public void sendFrequencyItemPacket(int frequency, Hand hand)
	{
		ModNetworking.INSTANCE.sendToServer(new FrequencyItemPacket.SetFrequency(frequency, hand));
	}

	@Override
	public void sendFrequencyBlockPacket(int frequency, BlockPos pos)
	{
		ModNetworking.INSTANCE.sendToServer(new FrequencyBlockPacket.SetFrequency(frequency, pos));
	}
}
