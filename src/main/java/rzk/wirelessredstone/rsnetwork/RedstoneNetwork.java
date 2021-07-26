package rzk.wirelessredstone.rsnetwork;

import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.util.Constants;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.registry.ModBlocks;

public class RedstoneNetwork extends SavedData
{
	public static final String DATA_NAME = "redstone_network";

	private final Short2ObjectMap<Channel> channels;
	private ServerLevel world;

	public RedstoneNetwork()
	{
		channels = new Short2ObjectOpenHashMap<>();
	}

	public static RedstoneNetwork get(ServerLevel world)
	{
		if (world == null)
			return null;

		RedstoneNetwork instance = world.getDataStorage().get(RedstoneNetwork::load, DATA_NAME);

		if (instance == null)
		{
			instance = new RedstoneNetwork();
			world.getDataStorage().set(DATA_NAME, instance);
		}

		instance.world = world;

		return instance;
	}

	void setReceiverState(BlockPos pos, boolean state)
	{
		if (world.isLoaded(pos) && world.getBlockState(pos).is(ModBlocks.redstoneReceiver))
			BlockFrequency.setPoweredState(world, pos, state);
	}

	public void updateReceivers(short frequency)
	{
		if (channels.containsKey(frequency))
		{
			Channel channel = channels.get(frequency);
			boolean isActive = channel.isActive();

			for (BlockPos receiver : channel.getReceivers())
				setReceiverState(receiver, isActive);
		}
	}

	public void addDevice(Device device)
	{
		if (device == null)
			return;

		short frequency = device.getFrequency();
		Channel channel = channels.get(frequency);

		if (channel == null)
		{
			channel = Channel.create(frequency);
			channels.put(frequency, channel);
		}

		channel.addDevice(device);

		if (device.isSender())
			updateReceivers(frequency);
		else if (device.isReceiver() && device.isBlock() && channel.isActive())
			setReceiverState(((Device.Block) device).getFreqPos(), true);

		setDirty();
	}

	public void removeDevice(Device device)
	{
		if (device == null)
			return;

		short frequency = device.getFrequency();
		Channel channel = channels.get(frequency);

		if (channel != null)
		{
			channel.removeDevice(device);

			if (device.isSender())
				updateReceivers(frequency);

			setDirty();
		}
	}

	public void changeDeviceFrequency(Device device, short newFrequency)
	{
		if (device == null)
			return;

		Device.Type type = device.getDeviceType();
		removeDevice(device);
		BlockPos pos = null;

		if (device.isBlock())
			pos = ((Device.Block) device).getFreqPos();

		addDevice(Device.create(newFrequency, type, pos, null));

		if (device.isReceiver())
			setReceiverState(pos, isChannelActive(newFrequency));
	}

	public boolean isChannelActive(short frequency)
	{
		Channel channel = channels.get(frequency);
		return channel != null && channel.isActive();
	}

	public void clearFrequency(short frequency)
	{
		Channel channel = channels.get(frequency);

		if (channel != null)
		{
			channel.clear();
			updateReceivers(frequency);
			setDirty();
		}
	}

	public void clearAll()
	{
		for (Channel channel : channels.values())
		{
			channel.clear();
			updateReceivers(channel.getFrequency());
		}

		setDirty();
	}

	public Channel getChannel(short frequency)
	{
		return channels.get(frequency);
	}

	private static RedstoneNetwork load(CompoundTag nbt)
	{
		RedstoneNetwork network = new RedstoneNetwork();

		if (nbt.contains("channels"))
		{
			ListTag channelsNBT = nbt.getList("channels", Constants.NBT.TAG_COMPOUND);

			for (Tag tag : channelsNBT)
			{
				Channel channel = Channel.fromNBT((CompoundTag) tag);

				if (channel != null && !channel.isEmpty())
					network.channels.put(channel.getFrequency(), channel);
			}
		}

		return network;
	}

	@Override
	public CompoundTag save(CompoundTag nbt)
	{
		if (!channels.isEmpty())
		{
			channels.short2ObjectEntrySet().removeIf(entry -> entry.getValue().isEmpty());
			ListTag channelNBT = new ListTag();

			for (Channel channel : channels.values())
				channelNBT.add(channel.toNBT());

			nbt.put("channels", channelNBT);
		}

		return nbt;
	}
}
