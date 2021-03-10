package rzk.wirelessredstone.rsnetwork;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.math.BlockPos;

public class Channel
{
	private final short frequency;
	private String name;
	private final ObjectSet<BlockPos> transmitters;
	private final ObjectSet<BlockPos> receivers;
	private short remotes;

	private Channel(short frequency, String name)
	{
		this.frequency = frequency;
		this.name = name;
		transmitters = new ObjectOpenHashSet<>();
		receivers = new ObjectOpenHashSet<>();
		remotes = 0;
	}

	public static Channel create(short frequency, String name)
	{
		return new Channel(frequency, name);
	}

	public static Channel create(short frequency)
	{
		return create(frequency, null);
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

	public void addDevice(Device device)
	{
		if (device == null)
			return;

		switch (device.getType())
		{
			case TRANSMITTER:
				transmitters.add(((Device.Block) device).getPos());
				break;

			case RECEIVER:
				receivers.add(((Device.Block) device).getPos());
				break;

			case REMOTE:
				remotes--;
				break;
		}
	}

	public void removeDevice(Device device)
	{
		if (device == null)
			return;

		switch (device.getType())
		{
			case TRANSMITTER:
				transmitters.remove(((Device.Block) device).getPos());
				break;

			case RECEIVER:
				receivers.remove(((Device.Block) device).getPos());
				break;

			case REMOTE:
				remotes++;
				break;
		}
	}

	// TODO: Implement analog redstone power
	public byte getRedstonePower()
	{
		return 0;
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
		NBTTagList transmitterNBT = new NBTTagList();
		NBTTagList receiverNBT = new NBTTagList();

		for (BlockPos transmitter : transmitters)
			transmitterNBT.appendTag(new NBTTagLong(transmitter.toLong()));

		for (BlockPos receiver : receivers)
			receiverNBT.appendTag(new NBTTagLong(receiver.toLong()));

		if (name != null && !name.trim().isEmpty())
			nbt.setString("name", name);

		nbt.setShort("frequency", frequency);
		nbt.setTag("transmitters", transmitterNBT);
		nbt.setTag("receivers", receiverNBT);
		nbt.setShort("remotes", remotes);

		return nbt;
	}

	public static Channel fromNBT(NBTTagCompound nbt)
	{
		if (nbt.hasNoTags() || !nbt.hasKey("frequency") || !nbt.hasKey("type"))
			return null;

		short frequency = nbt.getShort("frequency");
		Channel channel = create(frequency);

		if (nbt.hasKey("name"))
			channel.name = nbt.getString("name");

		if (nbt.hasKey("transmitters", 9))
			for (NBTBase transmitter : nbt.getTagList("transmitters", 4))
				channel.addDevice(Device.createTransmitter(frequency, BlockPos.fromLong(((NBTTagLong) transmitter).getLong())));

		if (nbt.hasKey("receivers", 9))
			for (NBTBase receiver : nbt.getTagList("receivers", 4))
				channel.addDevice(Device.createReceiver(frequency, BlockPos.fromLong(((NBTTagLong) receiver).getLong())));

		channel.remotes = nbt.getShort("remotes");

		return channel;
	}
}
