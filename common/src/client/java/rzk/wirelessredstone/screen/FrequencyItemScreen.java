package rzk.wirelessredstone.screen;

import net.minecraft.util.Hand;
import rzk.wirelessredstone.WirelessRedstoneClient;

public class FrequencyItemScreen extends FrequencyScreen
{
	private final Hand hand;

	public FrequencyItemScreen(int frequency, Hand hand)
	{
		super(frequency);
		this.hand = hand;
	}

	@Override
	protected void setFrequency()
	{
		WirelessRedstoneClient.PLATFORM.sendFrequencyItemPacket(getInputFrequency(), hand);
	}
}
