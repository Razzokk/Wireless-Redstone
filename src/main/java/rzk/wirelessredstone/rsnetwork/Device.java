package rzk.wirelessredstone.rsnetwork;

import net.minecraft.util.math.BlockPos;

public interface Device
{
	short getFrequency();

	Type getType();

	interface Block extends Device
	{
		BlockPos getPos();
	}

	enum Type
	{
		TRANSMITTER,
		RECEIVER,
		REMOTE
	}

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
}
