package rzk.wirelessredstone.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.WirelessRedstone;

public abstract class PacketFrequency implements Packet
{
	protected short frequency;
	protected boolean extended;
	protected BlockPos pos;
	protected Hand hand;
	protected Type type;

	public PacketFrequency(short frequency, boolean extended)
	{
		this.frequency = frequency;
		this.extended = extended;
	}

	public PacketFrequency(short frequency, PlayerEntity player)
	{
		this(frequency, false);

		CompoundNBT nbt = player.getPersistentData();

		if (nbt.contains(WirelessRedstone.MOD_ID + ".extended"))
			extended = nbt.getBoolean(WirelessRedstone.MOD_ID + ".extended");
	}

	public PacketFrequency(short frequency, boolean extended, BlockPos pos)
	{
		this(frequency, extended);
		this.pos = pos;
		type = Type.BLOCK;
	}

	public PacketFrequency(short frequency, boolean extended, Hand hand)
	{
		this(frequency, extended);
		this.hand = hand;
		type = Type.ITEM;
	}

	public PacketFrequency(PacketBuffer buffer)
	{
		frequency = buffer.readShort();
		extended = buffer.readBoolean();
		type = Type.byIndex(buffer.readInt());

		switch (type)
		{
			case BLOCK:
				pos = BlockPos.of(buffer.readLong());
				break;
			case ITEM:
				hand = buffer.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
				break;
		}
	}

	@Override
	public void toBytes(PacketBuffer buffer)
	{
		buffer.writeShort(frequency);
		buffer.writeBoolean(extended);
		buffer.writeInt(type.getIndex());

		switch (type)
		{
			case BLOCK:
				buffer.writeLong(pos.asLong());
				break;
			case ITEM:
				buffer.writeBoolean(hand == Hand.MAIN_HAND);
				break;
		}
	}

	public short getFrequency()
	{
		return frequency;
	}

	public boolean isExtended()
	{
		return extended;
	}

	public Type getType()
	{
		return type;
	}

	public BlockPos getPos()
	{
		return pos;
	}

	public Hand getHand()
	{
		return hand;
	}

	public enum Type
	{
		BLOCK(0),
		ITEM(1);

		private int index;

		Type(int index)
		{
			this.index = index;
		}

		public int getIndex()
		{
			return index;
		}

		public static Type byIndex(int index)
		{
			for (Type type : Type.values())
				if (type.index == index)
					return type;

			return null;
		}
	}
}
