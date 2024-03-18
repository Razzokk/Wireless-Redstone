package rzk.wirelessredstone.platform;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.network.PacketDistributor;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.SnifferHighlightPacket;

import java.io.File;

public class PlatformNeo implements Platform
{
	@Override
	public PlatformLoader getLoader()
	{
		return PlatformLoader.NEOFORGE;
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
		PacketDistributor.PLAYER.with(player).send(new FrequencyItemPacket(frequency, hand));
	}

	@Override
	public void sendFrequencyBlockPacket(ServerPlayerEntity player, int frequency, BlockPos pos)
	{
		PacketDistributor.PLAYER.with(player).send(new FrequencyBlockPacket(frequency, pos));
	}

	@Override
	public void sendSniffer(ServerPlayerEntity player, long time, Hand hand, BlockPos[] transmitters)
	{
		PacketDistributor.PLAYER.with(player).send(new SnifferHighlightPacket(time, hand, transmitters));
	}
}
