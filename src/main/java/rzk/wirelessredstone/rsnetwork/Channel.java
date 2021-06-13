package rzk.wirelessredstone.rsnetwork;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class Channel
{
	private final short frequency;
	private final ObjectSet<BlockPos> transmitters;
	private final ObjectSet<BlockPos> receivers;
	private String name;
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

	public static Channel fromNBT(CompoundNBT nbt)
	{
		if (nbt.isEmpty() || !nbt.contains("frequency"))
			return null;

		short frequency = nbt.getShort("frequency");
		Channel channel = create(frequency);

		if (nbt.contains("name"))
			channel.name = nbt.getString("name");

		if (nbt.contains("transmitters"))
			for (long transmitter : nbt.getLongArray("transmitters"))
				channel.addDevice(Device.createTransmitter(frequency, BlockPos.of(transmitter)));

		if (nbt.contains("receivers"))
			for (long receiver : nbt.getLongArray("receivers"))
				channel.addDevice(Device.createReceiver(frequency, BlockPos.of(receiver)));

		channel.remotes = nbt.getShort("remotes");

		return channel;
	}

	public void clear()
	{
		transmitters.clear();
		remotes = 0;
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

		if (device.isBlock())
		{
			BlockPos pos = ((Device.Block) device).getFreqPos();

			if (device.isTransmitter())
				transmitters.add(pos);
			else
				receivers.add(pos);
		}

		if (device.isRemote())
			remotes++;
	}

	public void removeDevice(Device device)
	{
		if (device == null)
			return;

		if (device.isBlock())
		{
			BlockPos pos = ((Device.Block) device).getFreqPos();

			if (device.isTransmitter())
				transmitters.remove(pos);
			else
				receivers.remove(pos);
		}

		if (device.isRemote() && remotes > 0)
			remotes--;
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

	public CompoundNBT toNBT()
	{
		CompoundNBT nbt = new CompoundNBT();

		if (name != null && !name.trim().isEmpty())
			nbt.putString("name", name);

		nbt.putShort("frequency", frequency);
		nbt.putLongArray("transmitters", transmitters.stream().mapToLong(BlockPos::asLong).toArray());
		nbt.putLongArray("receivers", receivers.stream().mapToLong(BlockPos::asLong).toArray());
		nbt.putShort("remotes", remotes);

		return nbt;
	}
}
