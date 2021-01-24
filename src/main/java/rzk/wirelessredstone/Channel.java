package rzk.wirelessredstone;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
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
	// active remotes (power state: on)
	private short remotes;

	private Channel(short frequency, Type type, String name)
	{
		this.frequency = frequency;
		this.type = type;
		this.name = name;
		transmitters = new ObjectArraySet<>(1);
		receivers = new ObjectArraySet<>(1);
		remotes = 0;
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

	public short getRemotes()
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

	public void addRemote()
	{
		remotes++;
	}

	public void removeRemote()
	{
		if (remotes > 0)
			remotes--;
	}

	public boolean isActive()
	{
		return !transmitters.isEmpty() || remotes > 0;
	}

	public boolean isEmpty()
	{
		return transmitters.isEmpty() && receivers.isEmpty() && remotes <= 0 && (name == null || name.trim().isEmpty());
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

		nbt.setShort("remotes", remotes);

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

		channel.remotes = nbt.getShort("remotes");

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
