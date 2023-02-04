package rzk.wirelessredstone.client.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Hand;
import rzk.wirelessredstone.network.FrequencyItemPacket;

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
		ClientPlayNetworking.send(FrequencyItemPacket.ID, new FrequencyItemPacket(getInputFrequency(), hand).toPacketByteBuf());
	}
}
