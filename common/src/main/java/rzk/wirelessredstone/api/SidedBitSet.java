package rzk.wirelessredstone.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;

public class SidedBitSet
{
	public static final byte UP = sideMask(Direction.UP);
	public static final byte DOWN = sideMask(Direction.DOWN);
	public static final byte NORTH = sideMask(Direction.NORTH);
	public static final byte SOUTH = sideMask(Direction.SOUTH);
	public static final byte EAST = sideMask(Direction.EAST);
	public static final byte WEST = sideMask(Direction.WEST);

	private byte bits;

	public SidedBitSet(byte bits)
	{
		this.bits = bits;
	}

	public SidedBitSet(String nbtKey, NbtCompound nbt)
	{
		this.bits = nbt.getByte(nbtKey);
	}

	public static byte sideMask(Direction side)
	{
		return (byte) (1 << side.getId());
	}

	public static SidedBitSet allSet()
	{
		return new SidedBitSet((byte) (UP | DOWN | NORTH | SOUTH | EAST | WEST));
	}

	public void saveNbt(String name, NbtCompound nbt)
	{
		nbt.putByte(name, bits);
	}

	public boolean get(Direction side)
	{
		return (bits & sideMask(side)) != 0;
	}

	public void setBit(Direction side)
	{
		bits |= sideMask(side);
	}

	public void unsetBit(Direction side)
	{
		bits &= (byte) ~sideMask(side);
	}

	public void toggleBit(Direction side)
	{
		bits ^= sideMask(side);
	}
}
