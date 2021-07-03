package rzk.wirelessredstone.rsnetwork;

import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public interface Device
{
	static Device create(short frequency, Type type, BlockPos pos, Hand hand)
	{
		if (type == Type.REMOTE)
			return new DeviceImpl.Item(frequency, type, hand);

		return new DeviceImpl.Block(frequency, type, pos);
	}

	static Device createTransmitter(short frequency, BlockPos pos)
	{
		return create(frequency, Type.TRANSMITTER, pos, null);
	}

	static Device createReceiver(short frequency, BlockPos pos)
	{
		return create(frequency, Type.RECEIVER, pos, null);
	}

	static Device createRemote(short frequency, Hand hand)
	{
		return create(frequency, Type.REMOTE, null, hand);
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

	interface Item extends Device
	{
		Hand getHand();
	}
}
