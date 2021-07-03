package rzk.wirelessredstone.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.ItemFrequency;
import rzk.wirelessredstone.tile.TileFrequency;

import java.util.function.Supplier;

public class PacketSetFrequency implements Packet
{
	protected short frequency;
	protected boolean extended;
	protected BlockPos pos;
	protected Hand hand;
	protected boolean isBlock;

	public PacketSetFrequency(short frequency, boolean extended, BlockPos pos)
	{
		this.frequency = frequency;
		this.extended = extended;
		this.pos = pos;
		isBlock = true;
	}

	public PacketSetFrequency(short frequency, boolean extended, Hand hand)
	{
		this.frequency = frequency;
		this.extended = extended;
		this.hand = hand;
		isBlock = false;
	}

	public PacketSetFrequency(PacketBuffer buffer)
	{
		frequency = buffer.readShort();
		extended = buffer.readBoolean();
		isBlock = buffer.readBoolean();

		if (isBlock)
			pos = buffer.readBlockPos();
		else
			hand = buffer.readBoolean() ? Hand.MAIN_HAND : Hand.OFF_HAND;
	}

	@Override
	public void toBytes(PacketBuffer buffer)
	{
		buffer.writeShort(frequency);
		buffer.writeBoolean(extended);
		buffer.writeBoolean(isBlock);

		if (isBlock)
			buffer.writeBlockPos(pos);
		else
			buffer.writeBoolean(hand == Hand.MAIN_HAND);
	}

	public short getFrequency()
	{
		return frequency;
	}

	public boolean isExtended()
	{
		return extended;
	}

	public boolean isBlock()
	{
		return isBlock;
	}

	public BlockPos getPos()
	{
		return pos;
	}

	public Hand getHand()
	{
		return hand;
	}

	@Override
	public boolean handle(Supplier<NetworkEvent.Context> ctx)
	{
		if (ctx.get().getDirection().getReceptionSide().isServer())
		{
			ServerPlayerEntity player = ctx.get().getSender();
			ServerWorld world = player.getLevel();

			ctx.get().enqueueWork(() ->
			{
				if (isBlock)
				{
					TileEntity tile = world.getBlockEntity(pos);
					if (tile instanceof TileFrequency)
						((TileFrequency) tile).setFrequency(frequency);
				}
				else
				{
					ItemStack stack = player.getItemInHand(hand);
					if (stack.getItem() instanceof ItemFrequency)
						ItemFrequency.setFrequency(stack, frequency);
				}

				CompoundNBT nbt = player.getPersistentData();
				nbt.putBoolean(WirelessRedstone.MOD_ID + ".extended", extended);
			});
		}

		return true;
	}
}
