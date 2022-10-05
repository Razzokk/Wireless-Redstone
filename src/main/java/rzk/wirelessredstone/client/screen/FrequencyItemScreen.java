package rzk.wirelessredstone.client.screen;

import net.minecraft.world.InteractionHand;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.PacketHandler;

public class FrequencyItemScreen extends FrequencyScreen
{
	private final InteractionHand hand;

	public FrequencyItemScreen(int frequency, InteractionHand hand)
	{
		super(frequency);
		this.hand = hand;
	}

	@Override
	protected void setFrequency()
	{
		PacketHandler.INSTANCE.sendToServer(new FrequencyItemPacket(getInputFrequency(), hand));
	}
}
