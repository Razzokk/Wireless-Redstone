package rzk.wirelessredstone.network;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.ItemFrequency;
import rzk.wirelessredstone.tile.TileFrequency;

import java.util.function.Supplier;

public class PacketSetFrequency implements Packet
{
	protected short frequency;
	protected boolean extended;
	protected BlockPos pos;
	protected InteractionHand hand;
	protected boolean isBlock;

	public PacketSetFrequency(short frequency, boolean extended, BlockPos pos)
	{
		this.frequency = frequency;
		this.extended = extended;
		this.pos = pos;
		isBlock = true;
	}

	public PacketSetFrequency(short frequency, boolean extended, InteractionHand hand)
	{
		this.frequency = frequency;
		this.extended = extended;
		this.hand = hand;
		isBlock = false;
	}

	public PacketSetFrequency(FriendlyByteBuf buffer)
	{
		frequency = buffer.readShort();
		extended = buffer.readBoolean();
		isBlock = buffer.readBoolean();

		if (isBlock)
			pos = buffer.readBlockPos();
		else
			hand = buffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
	}

	@Override
	public void toBytes(FriendlyByteBuf buffer)
	{
		buffer.writeShort(frequency);
		buffer.writeBoolean(extended);
		buffer.writeBoolean(isBlock);

		if (isBlock)
			buffer.writeBlockPos(pos);
		else
			buffer.writeBoolean(hand == InteractionHand.MAIN_HAND);
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

	public InteractionHand getHand()
	{
		return hand;
	}

	@Override
	public boolean handle(Supplier<NetworkEvent.Context> ctx)
	{
		if (ctx.get().getDirection().getReceptionSide().isServer())
		{
			ServerPlayer player = ctx.get().getSender();
			ServerLevel world = player.getLevel();

			ctx.get().enqueueWork(() ->
			{
				if (isBlock && world.getBlockEntity(pos) instanceof TileFrequency tile)
				{
					tile.setFrequency(frequency);
				}
				else
				{
					ItemStack stack = player.getItemInHand(hand);
					if (stack.getItem() instanceof ItemFrequency)
						ItemFrequency.setFrequency(stack, frequency);
				}

				CompoundTag nbt = player.getPersistentData();
				nbt.putBoolean(WirelessRedstone.MOD_ID + ".extended", extended);
			});
		}

		return true;
	}
}
