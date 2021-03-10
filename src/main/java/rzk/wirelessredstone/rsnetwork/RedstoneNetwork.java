package rzk.wirelessredstone.rsnetwork;

import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
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
	private Short2ObjectMap<Channel> basic;

	public RedstoneNetwork(String name)
	{
		super(name);
	}

	public void updateReceivers(short frequency)
	{
		if (basic.containsKey(frequency))
		{
			Channel channel = basic.get(frequency);
			boolean isActive = channel.isActive();

			for (BlockPos receiver : channel.getReceivers())
				if (world.isBlockLoaded(receiver))
					((BlockFrequency) ModBlocks.redstoneReceiver).setPoweredState(world.getBlockState(receiver), world, receiver, isActive);
		}
	}

	public void addDevice(Device device)
	{
		if (device == null)
			return;

		short frequency = device.getFrequency();
		Device.Type type = device.getType();
		Channel channel = basic.get(frequency);

		if (channel == null)
		{
			channel = Channel.create(frequency);
			basic.put(frequency, channel);
		}

		channel.addDevice(device);

		if (type == Device.Type.TRANSMITTER || type == Device.Type.REMOTE)
			updateReceivers(frequency);

		markDirty();
	}

	public void removeDevice(Device device)
	{
		if (device == null)
			return;

		short frequency = device.getFrequency();
		Device.Type type = device.getType();
		Channel channel = basic.get(frequency);

		if (channel != null)
		{
			channel.removeDevice(device);

			if (type == Device.Type.TRANSMITTER)
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
		addDevice(Device.create(newFrequency, type, null));

		if (type == Device.Type.RECEIVER)
		{
			BlockPos pos = ((Device.Block) device).getPos();
			((BlockFrequency) ModBlocks.redstoneReceiver).setPoweredState(world.getBlockState(pos), world, pos, basic.get(newFrequency).isActive());
		}
	}

	public boolean isChannelActive(short frequency)
	{
		Channel channel = basic.get(frequency);
		return channel != null && channel.isActive();
	}

	public RedstoneNetwork()
	{
		this(DATA_NAME);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasKey("basic"))
		{
			NBTTagList basicNBT = nbt.getTagList("basic", 10);

			for (NBTBase channelNBT : basicNBT)
			{
				Channel channel = Channel.fromNBT((NBTTagCompound) channelNBT);

				if (channel != null && !channel.isEmpty())
					basic.put(channel.getFrequency(), channel);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if (!basic.isEmpty())
		{
			NBTTagList basicNBT = new NBTTagList();
			basic.short2ObjectEntrySet().removeIf(entry -> entry.getValue().isEmpty());

			for (Channel channel : basic.values())
				basicNBT.appendTag(channel.toNBT());

			nbt.setTag("basic", basicNBT);
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
