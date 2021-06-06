package rzk.wirelessredstone.rsnetwork;

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
	public Type getType()
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
}
