package rzk.wirelessredstone.platform;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

import java.io.File;

public interface Platform
{
	PlatformLoader getLoader();

	File getConfigDir();

	boolean isModLoaded(String modId);

	void sendFrequencyItemPacket(ServerPlayerEntity player, int frequency, Hand hand);

	void sendFrequencyBlockPacket(ServerPlayerEntity player, int frequency, BlockPos pos);

	void sendSniffer(ServerPlayerEntity player, long time, Hand hand, BlockPos[] transmitters);
}
