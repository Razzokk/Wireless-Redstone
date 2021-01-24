package rzk.wirelessredstone.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.client.gui.GuiFrequency;
import rzk.wirelessredstone.item.ItemFrequency;
import rzk.wirelessredstone.tile.TileFrequency;

public class PacketFrequency implements IMessage
{
	private short frequency;
	private boolean extended;
	private BlockPos pos;
	private EnumHand hand;
	private Type type;

	public PacketFrequency() {}

	private PacketFrequency(short frequency, boolean extended)
	{
		this.frequency = frequency;
		this.extended = extended;
	}

	private PacketFrequency(short frequency, EntityPlayer player)
	{
		this(frequency, false);

		NBTTagCompound nbt = player.getEntityData();

		if (nbt.hasKey(WirelessRedstone.MOD_ID + ".extended"))
			extended = nbt.getBoolean(WirelessRedstone.MOD_ID + ".extended");
	}

	public PacketFrequency(short frequency, boolean extended, BlockPos pos)
	{
		this(frequency, extended);
		this.pos = pos;
		type = Type.BLOCK;
	}

	public PacketFrequency(short frequency, boolean extended, EnumHand hand)
	{
		this(frequency, extended);
		this.hand = hand;
		type = Type.ITEM;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		frequency = buf.readShort();
		extended = buf.readBoolean();
		type = Type.byIndex(buf.readInt());

		switch (type)
		{
			case BLOCK:
				pos = BlockPos.fromLong(buf.readLong());
				break;
			case ITEM:
				hand = buf.readBoolean() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
				break;
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeShort(frequency);
		buf.writeBoolean(extended);
		buf.writeInt(type.getIndex());

		switch (type)
		{
			case BLOCK:
				buf.writeLong(pos.toLong());
				break;
			case ITEM:
				buf.writeBoolean(hand == EnumHand.MAIN_HAND);
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

	public EnumHand getHand()
	{
		return hand;
	}

	public static class PacketFrequencyHandler implements IMessageHandler<PacketFrequency, IMessage>
	{
		@Override
		public IMessage onMessage(PacketFrequency message, MessageContext ctx)
		{
			if (ctx.side.isServer())
			{
				EntityPlayerMP player = ctx.getServerHandler().player;
				WorldServer world = player.getServerWorld();
				world.addScheduledTask(() ->
				{
					switch (message.type)
					{
						case BLOCK:
							TileEntity tile = world.getTileEntity(message.pos);
							if (tile instanceof TileFrequency)
								((TileFrequency) tile).setFrequency(message.frequency);
							break;
						case ITEM:
							ItemStack stack = player.getHeldItem(message.hand);
							if (stack.getItem() instanceof ItemFrequency)
								ItemFrequency.setFrequency(stack, message.frequency);
							break;
					}

					NBTTagCompound nbt = player.getEntityData();
					nbt.setBoolean(WirelessRedstone.MOD_ID + ".extended", message.extended);
				});
			}
			return null;
		}
	}

	public static class PacketFrequencyOpenGuiHandler implements IMessageHandler<PacketFrequency, IMessage>
	{
		@Override
		public IMessage onMessage(PacketFrequency message, MessageContext ctx)
		{
			if (ctx.side.isClient())
				WirelessRedstone.proxy.openFrequencyGui(message);
			return null;
		}
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
