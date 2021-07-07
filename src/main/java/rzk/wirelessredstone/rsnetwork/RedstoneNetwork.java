package rzk.wirelessredstone.rsnetwork;

import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.registry.ModBlocks;

public class RedstoneNetwork extends WorldSavedData
{
	public static final String DATA_NAME = "redstone_network";

	private final Short2ObjectMap<Channel> channels;
	private ServerWorld world;

	public RedstoneNetwork(String name)
	{
		super(name);
		channels = new Short2ObjectOpenHashMap<>();
	}

	public RedstoneNetwork()
	{
		this(DATA_NAME);
	}

	public static RedstoneNetwork get(ServerWorld world)
	{
		if (world == null)
			return null;

		RedstoneNetwork instance = world.getDataStorage().get(RedstoneNetwork::new, DATA_NAME);

		if (instance == null)
		{
			instance = new RedstoneNetwork(DATA_NAME);
			world.getDataStorage().set(instance);
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

	@Override
	public void load(CompoundNBT nbt)
	{
		if (nbt.contains("channels"))
		{
			ListNBT channelsNBT = nbt.getList("channels", Constants.NBT.TAG_COMPOUND);

			for (INBT channelNBT : channelsNBT)
			{
				Channel channel = Channel.fromNBT((CompoundNBT) channelNBT);

				if (channel != null && !channel.isEmpty())
					channels.put(channel.getFrequency(), channel);
			}
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt)
	{
		if (!channels.isEmpty())
		{
			channels.short2ObjectEntrySet().removeIf(entry -> entry.getValue().isEmpty());
			ListNBT channelNBT = new ListNBT();

			for (Channel channel : channels.values())
				channelNBT.add(channel.toNBT());

			nbt.put("channels", channelNBT);
		}

		return nbt;
	}
}
