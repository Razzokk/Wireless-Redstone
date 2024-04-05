package rzk.wirelessredstone.platform;

import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.network.PacketDistributor;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.registry.ModNetworking;

public class ClientPlatformForge implements ClientPlatform
{
	@Override
	public void sendFrequencyItemPacket(int frequency, Hand hand)
	{
		ModNetworking.INSTANCE.send(new FrequencyItemPacket(frequency, hand), PacketDistributor.SERVER.noArg());
	}

	@Override
	public void sendFrequencyBlockPacket(int frequency, BlockPos pos)
	{
		ModNetworking.INSTANCE.send(new FrequencyBlockPacket(frequency, pos), PacketDistributor.SERVER.noArg());
	}
}
