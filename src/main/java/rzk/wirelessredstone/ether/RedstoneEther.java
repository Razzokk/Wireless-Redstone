package rzk.wirelessredstone.ether;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class RedstoneEther extends SavedData
{
	private static RedstoneEther instance;
	private final Int2ObjectMap<RedstoneChannel> ether = new Int2ObjectOpenHashMap<>();

	private RedstoneEther() {}

	@Override
	public CompoundTag save(CompoundTag tag)
	{
		return null;
	}

	public static RedstoneEther instance()
	{
		if (instance == null)
			instance = new RedstoneEther();
		return instance;
	}

	private RedstoneChannel getChannel(int freq)
	{
		RedstoneChannel channel = ether.get(freq);

		if (channel == null)
		{
			channel = new RedstoneChannel(freq);
			ether.put(freq, channel);
		}

		return channel;
	}

	public void addTransmitter(Level level, int freq, BlockPos pos)
	{
		RedstoneChannel channel = getChannel(freq);
		channel.addTransmitter(level, pos);
	}

	public void addReceiver(Level level, int freq, BlockPos pos)
	{
		RedstoneChannel channel = getChannel(freq);
		channel.addReceiver(level, pos);
	}

	public void removeTransmitter(Level level, int freq, BlockPos pos)
	{
		RedstoneChannel channel = ether.get(freq);
		if (channel != null)
			channel.removeTransmitter(level, pos);
	}

	public void removeReceiver(Level level, int freq, BlockPos pos)
	{
		RedstoneChannel channel = ether.get(freq);
		if (channel != null)
			channel.removeReceiver(level, pos);
	}

	public boolean isFreqActive(int freq)
	{
		RedstoneChannel channel = ether.get(freq);
		if (channel == null) return false;
		return channel.isActive();
	}
}
