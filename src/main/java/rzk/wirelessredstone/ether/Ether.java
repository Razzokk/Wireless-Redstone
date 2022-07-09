package rzk.wirelessredstone.ether;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class Ether
{
	private static Ether instance;
	private final Int2ObjectMap<Channel> ether = new Int2ObjectOpenHashMap<>();

	private Ether() {}

	public static Ether instance()
	{
		if (instance == null)
			instance = new Ether();
		return instance;
	}

	private Channel getChannel(int freq)
	{
		Channel channel = ether.get(freq);

		if (channel == null)
		{
			channel = new Channel(freq);
			ether.put(freq, channel);
		}

		return channel;
	}

	public void addTransmitter(Level level, int freq, BlockPos pos)
	{
		Channel channel = getChannel(freq);
		channel.addTransmitter(level, pos);
	}

	public void addReceiver(Level level, int freq, BlockPos pos)
	{
		Channel channel = getChannel(freq);
		channel.addReceiver(level, pos);
	}

	public void removeTransmitter(Level level, int freq, BlockPos pos)
	{
		Channel channel = ether.get(freq);
		if (channel != null)
			channel.removeTransmitter(level, pos);
	}

	public void removeReceiver(Level level, int freq, BlockPos pos)
	{
		Channel channel = ether.get(freq);
		if (channel != null)
			channel.removeReceiver(level, pos);
	}

	public boolean isFreqActive(int freq)
	{
		Channel channel = ether.get(freq);
		if (channel == null) return false;
		return channel.isActive();
	}
}
