package rzk.wirelessredstone;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.util.DeviceType;

public class Channel
{
	private final short frequency;
	private final Type type;
	private String name;
	// active transmitters (power state: on)
	private final ObjectSet<BlockPos> transmitters;
	private final ObjectSet<BlockPos> receivers;
	// active remote (power state: on)
	private final ObjectSet<ItemStack> remotes;

	private Channel(short frequency, Type type, String name)
	{
		this.frequency = frequency;
		this.type = type;
		this.name = name;
		transmitters = new ObjectArraySet<>(1);
		receivers = new ObjectArraySet<>(1);
		remotes = new ObjectArraySet<>(1);
	}

	public static Channel create(short frequency, Type type, String name)
	{
		return new Channel(frequency, type, name);
	}

	public static Channel create(short frequency, Type type)
	{
		return create(frequency, type, null);
	}

	public short getFrequency()
	{
		return frequency;
	}

	public ObjectSet<BlockPos> getTransmitters()
	{
		return transmitters;
	}

	public ObjectSet<BlockPos> getReceivers()
	{
		return receivers;
	}

	public ObjectSet<ItemStack> getRemotes()
	{
		return remotes;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void addDevice(BlockPos pos, DeviceType type)
	{
		switch (type)
		{
			case TRANSMITTER:
				transmitters.add(pos);
				break;

			case RECEIVER:
				receivers.add(pos);
				break;
		}
	}

	public void removeDevice(BlockPos pos, DeviceType type)
	{
		switch (type)
		{
			case TRANSMITTER:
				transmitters.remove(pos);
				break;

			case RECEIVER:
				receivers.remove(pos);
				break;
		}
	}

	public void addRemote(ItemStack stack)
	{
		remotes.add(stack);
	}

	public void removeRemote(ItemStack stack)
	{
		remotes.remove(stack);
	}

	public boolean isActive()
	{
		return !transmitters.isEmpty() || !remotes.isEmpty();
	}

	public boolean isEmpty()
	{
		return transmitters.isEmpty() && receivers.isEmpty() && remotes.isEmpty() && (name == null || name.trim().isEmpty());
	}

	public NBTTagCompound toNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setShort("frequency", frequency);
		nbt.setByte("type", (byte) type.index);

		NBTTagList transmitterNBT = new NBTTagList();
		transmitters.forEach(transmitter -> transmitterNBT.appendTag(new NBTTagLong(transmitter.toLong())));
		nbt.setTag("transmitters", transmitterNBT);

		NBTTagList receiverNBT = new NBTTagList();
		receivers.forEach(receiver -> receiverNBT.appendTag(new NBTTagLong(receiver.toLong())));
		nbt.setTag("receivers", receiverNBT);

		NBTTagList remoteNBT = new NBTTagList();
		remotes.forEach(remote -> remoteNBT.appendTag(remote.serializeNBT()));
		nbt.setTag("remotes", remoteNBT);

		return nbt;
	}

	public static Channel fromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasNoTags() || !nbt.hasKey("frequency") || !nbt.hasKey("type"))
			return null;

		Channel channel = create(nbt.getShort("frequency"), Type.byIndex(nbt.getByte("type")));

		if (nbt.hasKey("name"))
			channel.name = nbt.getString("name");

		if (nbt.hasKey("transmitters", 9))
		{
			NBTTagList transmitterNBT = nbt.getTagList("transmitters", 4);
			transmitterNBT.forEach(transmitter -> channel.addDevice(BlockPos.fromLong(((NBTTagLong) transmitter).getLong()), DeviceType.TRANSMITTER));
		}

		if (nbt.hasKey("receivers", 9))
		{
			NBTTagList receiverNBT = nbt.getTagList("receivers", 4);
			receiverNBT.forEach(receiver -> channel.addDevice(BlockPos.fromLong(((NBTTagLong) receiver).getLong()), DeviceType.RECEIVER));
		}

		if (nbt.hasKey("remotes", 9))
		{
			NBTTagList remoteNBT = nbt.getTagList("remotes", 10);
			remoteNBT.forEach(remote -> channel.addRemote(new ItemStack((NBTTagCompound) remote)));
		}

		return channel;
	}

	public enum Type
	{
		BASIC(0, "basic"),
		ANALOG(1, "analog"),
		BUNDLED(2, "bundled");

		private final int index;
		private final String name;

		Type(int index, String name)
		{
			this.index = index;
			this.name = name;
		}

		public int getIndex()
		{
			return index;
		}

		public String getName()
		{
			return name;
		}

		public static Type byIndex(int index)
		{
			for (Type type : values())
				if (type.getIndex() == index)
					return type;

			return null;
		}
	}
}
