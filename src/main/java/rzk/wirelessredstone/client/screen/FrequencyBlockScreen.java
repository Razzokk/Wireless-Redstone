package rzk.wirelessredstone.client.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.network.FrequencyBlockPacket;

public class FrequencyBlockScreen extends FrequencyScreen
{
	private final BlockPos pos;

	public FrequencyBlockScreen(int frequency, BlockPos pos)
	{
		super(frequency);
		this.pos = pos;
	}

	@Override
	protected void setFrequency()
	{
		ClientPlayNetworking.send(FrequencyBlockPacket.ID, new FrequencyBlockPacket(getInputFrequency(), pos).toPacketByteBuf());
	}
}
