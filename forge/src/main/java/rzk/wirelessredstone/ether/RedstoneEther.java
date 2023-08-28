package rzk.wirelessredstone.ether;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import rzk.wirelessredstone.misc.WRUtils;

import java.util.Collections;
import java.util.Set;

public class RedstoneEther extends SavedData
{
	private static final String DATA_NAME = "redstone_ether";
	private final Int2ObjectMap<RedstoneChannel> channels = new Int2ObjectOpenHashMap<>();

	private RedstoneEther() {}

	@Override
	public CompoundTag save(CompoundTag nbt)
	{
		ListTag channelNbts = new ListTag();
		for (RedstoneChannel channel : channels.values())
			channelNbts.add(channel.save());
		nbt.put("channels", channelNbts);
		return nbt;
	}

	private RedstoneEther(CompoundTag nbt)
	{
		ListTag channelTags = nbt.getList("channels", CompoundTag.TAG_COMPOUND);

		for (Tag channelNbt : channelTags)
		{
			RedstoneChannel channel = new RedstoneChannel((CompoundTag) channelNbt);
			channels.put(channel.getFrequency(), channel);
		}
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
		if (!WRUtils.isValidFrequency(frequency)) return;
		getOrCreateChannel(frequency).addTransmitter(level, pos);
		setDirty();
	}

	public void addRemote(Level level, LivingEntity owner, int frequency)
	{
		if (!WRUtils.isValidFrequency(frequency)) return;
		getOrCreateChannel(frequency).addRemote(level, owner);
	}

	public void addReceiver(Level level, BlockPos pos, int frequency)
	{
		if (!WRUtils.isValidFrequency(frequency)) return;
		getOrCreateChannel(frequency).addReceiver(level, pos);
	}

	public void removeTransmitter(Level level, BlockPos pos, int frequency)
	{
		RedstoneChannel channel = getChannel(frequency);
		if (channel == null) return;

		channel.removeTransmitter(level, pos);
		if (channel.isEmpty()) channels.remove(frequency);
		setDirty();
	}

	public void removeRemote(Level level, LivingEntity owner, int frequency)
	{
		RedstoneChannel channel = getChannel(frequency);
		if (channel == null) return;

		channel.removeRemote(level, owner);
		if (channel.isEmpty())
		{
			channels.remove(frequency);
			setDirty();
		}
	}

	public void removeReceiver(BlockPos pos, int frequency)
	{
		RedstoneChannel channel = getChannel(frequency);
		if (channel == null) return;

		channel.removeReceiver(pos);
		if (channel.isEmpty())
		{
			channels.remove(frequency);
			setDirty();
		}
	}

	public Set<BlockPos> getTransmitters(int frequency)
	{
		RedstoneChannel channel = getChannel(frequency);
		return channel != null ? channel.getTransmitters() : Collections.emptySet();
	}

	public boolean isFrequencyActive(int frequency)
	{
		RedstoneChannel channel = getChannel(frequency);
		return channel != null && channel.isActive();
	}
}
