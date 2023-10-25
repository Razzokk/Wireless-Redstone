package rzk.wirelessredstone.platform;

import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public interface ClientPlatform
{
	void sendFrequencyItemPacket(int frequency, Hand hand);

	void sendFrequencyBlockPacket(int frequency, BlockPos pos);
}
