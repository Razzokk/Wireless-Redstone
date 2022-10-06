package rzk.wirelessredstone.ether;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import rzk.wirelessredstone.misc.Utils;

public class RedstoneEther extends SavedData
{
	private static final String DATA_NAME = "redstone_ether";
	private final Int2ObjectMap<RedstoneChannel> channels = new Int2ObjectOpenHashMap<>();

	private RedstoneEther() {}

	private RedstoneEther(CompoundTag tag)
	{
		ListTag channelTags = tag.getList("channels", Tag.TAG_COMPOUND);

		for (Tag channelTag : channelTags)
		{
			RedstoneChannel channel = new RedstoneChannel((CompoundTag) channelTag);
			channels.put(channel.getFrequency(), channel);
		}
	}

	@Override
	public CompoundTag save(CompoundTag tag)
	{
		ListTag channelTags = new ListTag();
		for (RedstoneChannel channel : channels.values())
			channelTags.add(channel.save());
		tag.put("channels", channelTags);

		return tag;
	}

	public static RedstoneEther get(ServerLevel level)
	{
		return level.getDataStorage().get(RedstoneEther::new, DATA_NAME);
	}

	public static RedstoneEther getOrCreate(ServerLevel level)
	{
		return level.getDataStorage().computeIfAbsent(RedstoneEther::new, RedstoneEther::new, DATA_NAME);
	}

	private RedstoneChannel getChannel(int frequency)
	{
		return channels.get(frequency);
	}

	private RedstoneChannel getOrCreateChannel(int frequency)
	{
		RedstoneChannel channel = channels.get(frequency);

		if (channel == null)
		{
			channel = new RedstoneChannel(frequency);
			channels.put(frequency, channel);
		}

		return channel;
	}

	public void addTransmitter(Level level, BlockPos pos, int frequency)
	{
		if (!Utils.isValidFrequency(frequency)) return;

		RedstoneChannel channel = getOrCreateChannel(frequency);
		channel.addTransmitter(level, pos);
		setDirty();
	}

	public void addReceiver(Level level, BlockPos pos, int frequency)
	{
		if (!Utils.isValidFrequency(frequency)) return;

		RedstoneChannel channel = getOrCreateChannel(frequency);
		channel.addReceiver(level, pos);
	}

	public void removeTransmitter(Level level, BlockPos pos, int frequency)
	{
		RedstoneChannel channel = getChannel(frequency);
		if (channel != null)
		{
			channel.removeTransmitter(level, pos);
			if (channel.isEmpty()) channels.remove(frequency);
			setDirty();
		}
	}

	public void removeReceiver(BlockPos pos, int frequency)
	{
		RedstoneChannel channel = getChannel(frequency);
		if (channel != null)
		{
			channel.removeReceiver(pos);

			if (channel.isEmpty())
			{
				channels.remove(frequency);
				setDirty();
			}
		}
	}

	public boolean isFrequencyActive(int frequency)
	{
		RedstoneChannel channel = getChannel(frequency);
		return channel != null && channel.isActive();
	}
}
