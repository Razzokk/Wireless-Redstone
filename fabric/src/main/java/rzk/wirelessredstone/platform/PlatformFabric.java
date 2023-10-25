package rzk.wirelessredstone.platform;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.SnifferHighlightPacket;

import java.io.File;

public class PlatformFabric implements Platform
{
	@Override
	public PlatformLoader getLoader()
	{
		return PlatformLoader.FABRIC;
	}

	@Override
	public File getConfigDir()
	{
		return FabricLoader.getInstance().getConfigDir().toFile();
	}

	@Override
	public boolean isModLoaded(String modId)
	{
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public void sendFrequencyItemPacket(ServerPlayerEntity player, int frequency, Hand hand)
	{
		ServerPlayNetworking.send(player, new FrequencyItemPacket(frequency, hand));
	}

	@Override
	public void sendFrequencyBlockPacket(ServerPlayerEntity player, int frequency, BlockPos pos)
	{
		ServerPlayNetworking.send(player, new FrequencyBlockPacket(frequency, pos));
	}

	@Override
	public void sendSniffer(ServerPlayerEntity player, long time, Hand hand, BlockPos[] transmitters)
	{
		ServerPlayNetworking.send(player, new SnifferHighlightPacket(time, hand, transmitters));
	}
}
