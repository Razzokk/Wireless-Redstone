package rzk.wirelessredstone.platform;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.PacketDistributor;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.SnifferHighlightPacket;
import rzk.wirelessredstone.registry.ModNetworking;

import java.io.File;

public class PlatformForge implements Platform
{
	@Override
	public PlatformLoader getLoader()
	{
		return PlatformLoader.FORGE;
	}

	@Override
	public File getConfigDir()
	{
		return FMLPaths.CONFIGDIR.get().toFile();
	}

	@Override
	public boolean isModLoaded(String modId)
	{
		return ModList.get().isLoaded(modId);
	}

	@Override
	public void sendFrequencyItemPacket(ServerPlayerEntity player, int frequency, Hand hand)
	{
		ModNetworking.INSTANCE.send(new FrequencyItemPacket(frequency, hand), PacketDistributor.PLAYER.with(player));
	}

	@Override
	public void sendFrequencyBlockPacket(ServerPlayerEntity player, int frequency, BlockPos pos)
	{
		ModNetworking.INSTANCE.send(new FrequencyBlockPacket(frequency, pos), PacketDistributor.PLAYER.with(player));
	}

	@Override
	public void sendSniffer(ServerPlayerEntity player, long time, Hand hand, BlockPos[] transmitters)
	{
		ModNetworking.INSTANCE.send(new SnifferHighlightPacket(time, hand, transmitters), PacketDistributor.PLAYER.with(player));
	}
}
