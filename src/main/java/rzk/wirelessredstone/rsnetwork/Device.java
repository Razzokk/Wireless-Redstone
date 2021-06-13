package rzk.wirelessredstone.rsnetwork;

import net.minecraft.util.math.BlockPos;

public interface Device
{
	static Device create(short frequency, Type type, BlockPos pos)
	{
		if (type == Type.REMOTE)
			return new DeviceImpl(frequency, type);

		return new DeviceImpl.Block(frequency, type, pos);
	}

	static Device createTransmitter(short frequency, BlockPos pos)
	{
		return create(frequency, Type.TRANSMITTER, pos);
	}

	static Device createReceiver(short frequency, BlockPos pos)
	{
		return create(frequency, Type.RECEIVER, pos);
	}

	static Device createRemote(short frequency)
	{
		return create(frequency, Type.REMOTE, null);
	}

	short getFrequency();

	Type getDeviceType();

	default boolean isSender()
	{
		return getDeviceType() == Type.REMOTE || getDeviceType() == Type.TRANSMITTER;
	}

	default boolean isTransmitter()
	{
		return getDeviceType() == Type.TRANSMITTER;
	}

	default boolean isReceiver()
	{
		return getDeviceType() == Type.RECEIVER;
	}

	default boolean isRemote()
	{
		return getDeviceType() == Type.REMOTE;
	}

	default boolean isBlock()
	{
		return !isRemote();
	}

	enum Type
	{
		TRANSMITTER,
		RECEIVER,
		REMOTE
	}

	interface Block extends Device
	{
		BlockPos getFreqPos();
	}
}
