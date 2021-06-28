package rzk.wirelessredstone.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import rzk.wirelessredstone.client.gui.GuiFrequency;

import java.util.function.Supplier;

public class PacketFrequencyOpenGui extends PacketFrequency
{
	public PacketFrequencyOpenGui(short frequency, boolean extended)
	{
		super(frequency, extended);
	}

	public PacketFrequencyOpenGui(short frequency, PlayerEntity player)
	{
		super(frequency, player);
	}

	public PacketFrequencyOpenGui(short frequency, boolean extended, BlockPos pos)
	{
		super(frequency, extended, pos);
	}

	public PacketFrequencyOpenGui(short frequency, boolean extended, Hand hand)
	{
		super(frequency, extended, hand);
	}

	public PacketFrequencyOpenGui(PacketBuffer buffer)
	{
		super(buffer);
	}

	@Override
	public boolean handle(Supplier<NetworkEvent.Context> ctx)
	{
		if (ctx.get().getDirection().getReceptionSide().isClient())
			ctx.get().enqueueWork(() -> Minecraft.getInstance().setScreen(new GuiFrequency(this)));

		return true;
	}
}
