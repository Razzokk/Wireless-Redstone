package rzk.wirelessredstone.packet;

import net.minecraft.entity.player.PlayerEntity;
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

public class PacketSetFrequency extends PacketFrequency
{
	public PacketSetFrequency(short frequency, boolean extended)
	{
		super(frequency, extended);
	}

	public PacketSetFrequency(short frequency, PlayerEntity player)
	{
		super(frequency, player);
	}

	public PacketSetFrequency(short frequency, boolean extended, BlockPos pos)
	{
		super(frequency, extended, pos);
	}

	public PacketSetFrequency(short frequency, boolean extended, Hand hand)
	{
		super(frequency, extended, hand);
	}

	public PacketSetFrequency(PacketBuffer buffer)
	{
		super(buffer);
	}

	@Override
	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		if (ctx.get().getDirection().getReceptionSide().isServer())
		{
			ServerPlayerEntity player = ctx.get().getSender();
			ServerWorld world = player.getLevel();

			ctx.get().enqueueWork(() ->
			{
				switch (type)
				{
					case BLOCK:
						TileEntity tile = world.getBlockEntity(pos);
						if (tile instanceof TileFrequency)
							((TileFrequency) tile).setFrequency(frequency);
						break;
					case ITEM:
						ItemStack stack = player.getItemInHand(hand);
						if (stack.getItem() instanceof ItemFrequency)
							ItemFrequency.setFrequency(stack, frequency);
						break;
				}

				CompoundNBT nbt = player.getPersistentData();
				nbt.putBoolean(WirelessRedstone.MOD_ID + ".extended", extended);
			});
		}
	}
}
