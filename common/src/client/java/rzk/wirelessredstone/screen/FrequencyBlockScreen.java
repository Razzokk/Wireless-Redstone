package rzk.wirelessredstone.screen;

import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.WirelessRedstoneClient;

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
		WirelessRedstoneClient.PLATFORM.sendFrequencyBlockPacket(getInputFrequency(), pos);
	}
}
