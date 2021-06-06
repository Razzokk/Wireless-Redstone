package rzk.wirelessredstone.rsnetwork;

import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.registry.ModBlocks;

public class RedstoneNetwork extends WorldSavedData
{
	public static final String DATA_NAME = "redstoneNetwork";

	private World world;
	private final Short2ObjectMap<Channel> channels;

	public RedstoneNetwork(String name)
	{
		super(name);
		channels = new Short2ObjectOpenHashMap<>();
	}

	public RedstoneNetwork()
	{
		this(DATA_NAME);
	}

	void setReceiverState(BlockPos pos, boolean state)
	{
		if (world.isBlockLoaded(pos) && world.getBlockState(pos).getBlock() instanceof BlockFrequency)
			((BlockFrequency) ModBlocks.redstoneReceiver).setPoweredState(world.getBlockState(pos), world, pos, state);
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

		markDirty();
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

			markDirty();
		}
	}

	public void changeDeviceFrequency(Device device, short newFrequency)
	{
		if (device == null)
			return;

		Device.Type type = device.getType();
		removeDevice(device);
		BlockPos pos = null;

		if (device.isBlock())
			pos = ((Device.Block) device).getFreqPos();

		addDevice(Device.create(newFrequency, type, pos));

		if (type == Device.Type.RECEIVER)
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
		}
	}

	public void clearAll()
	{
		for (Channel channel : channels.values())
		{
			channel.clear();
			updateReceivers(channel.getFrequency());
		}
	}

	public Channel getChannel(short frequency)
	{
		return channels.get(frequency);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey("channels"))
		{
			NBTTagList channelsNBT = nbt.getTagList("channels", 10);

			for (NBTBase channelNBT : channelsNBT)
			{
				Channel channel = Channel.fromNBT((NBTTagCompound) channelNBT);

				if (channel != null && !channel.isEmpty())
					channels.put(channel.getFrequency(), channel);
			}
		}

		// DO NOT REMOVE: backwards compatibility
		if (nbt.hasKey("basic"))
		{
			NBTTagList channelsNBT = nbt.getTagList("basic", 10);

			for (NBTBase channelNBT : channelsNBT)
			{
				Channel channel = Channel.fromNBT((NBTTagCompound) channelNBT);

				if (channel != null && !channel.isEmpty())
					channels.put(channel.getFrequency(), channel);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if (!channels.isEmpty())
		{
			channels.short2ObjectEntrySet().removeIf(entry -> entry.getValue().isEmpty());
			NBTTagList channelNBT = new NBTTagList();

			for (Channel channel : channels.values())
				channelNBT.appendTag(channel.toNBT());

			nbt.setTag("channels", channelNBT);
		}

		return nbt;
	}

	public World getWorld()
	{
		return world;
	}

	public static RedstoneNetwork get(World world, boolean create)
	{
		if (world == null)
			return null;

		RedstoneNetwork instance = null;
		MapStorage mapStorage = world.getMapStorage();

		if (mapStorage != null)
		{
			instance = (RedstoneNetwork) mapStorage.getOrLoadData(RedstoneNetwork.class, DATA_NAME);

			if (create && instance == null)
			{
				instance = new RedstoneNetwork();
				mapStorage.setData(DATA_NAME, instance);
			}

			if (instance != null)
				instance.world = world;
		}

		return instance;
	}

	public static RedstoneNetwork get(World world)
	{
		return get(world, true);
	}
}
