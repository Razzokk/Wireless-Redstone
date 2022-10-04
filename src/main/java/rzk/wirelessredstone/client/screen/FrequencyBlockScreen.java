package rzk.wirelessredstone.client.screen;

import net.minecraft.core.BlockPos;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.PacketHandler;

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
		PacketHandler.INSTANCE.sendToServer(new FrequencyBlockPacket(10, pos));
	}
}
