package rzk.wirelessredstone.rsnetwork;

import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class DeviceImpl implements Device
{
	private final short frequency;
	private final Type type;

	public DeviceImpl(short frequency, Type type)
	{
		this.frequency = frequency;
		this.type = type;
	}

	@Override
	public short getFrequency()
	{
		return frequency;
	}

	@Override
	public Type getDeviceType()
	{
		return type;
	}

	public static class Block extends DeviceImpl implements Device.Block
	{
		private final BlockPos pos;

		public Block(short frequency, Type type, BlockPos pos)
		{
			super(frequency, type);
			this.pos = pos;
		}

		@Override
		public BlockPos getFreqPos()
		{
			return pos;
		}
	}

	public static class Item extends DeviceImpl implements Device.Item
	{
		private final Hand hand;

		public Item(short frequency, Type type, Hand hand)
		{
			super(frequency, type);
			this.hand = hand;
		}

		@Override
		public Hand getHand()
		{
			return hand;
		}
	}
}
