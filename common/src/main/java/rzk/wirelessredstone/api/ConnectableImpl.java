package rzk.wirelessredstone.api;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Direction;

public class ConnectableImpl implements Connectable
{
	private byte connections;

	private ConnectableImpl(byte connections)
	{
		this.connections = connections;
	}

	public ConnectableImpl(NbtCompound nbt)
	{
		this.connections = nbt.getByte("connections");
	}

	public static byte sideMask(Direction side)
	{
		return (byte) (1 << side.getId());
	}

	public static ConnectableImpl allSet()
	{
		return new ConnectableImpl((byte) 0b0011_1111);
	}

	public void saveNbt(NbtCompound nbt)
	{
		nbt.putByte("connections", connections);
	}

	@Override
	public boolean isConnectable(Direction side)
	{
		return (connections & sideMask(side)) != 0;
	}

	@Override
	public void toggleConnectable(Direction side)
	{
		connections ^= sideMask(side);
	}
}
