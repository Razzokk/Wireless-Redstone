package rzk.wirelessredstone.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Hand;
import rzk.wirelessredstone.network.FrequencyItemPacket;

@Environment(EnvType.CLIENT)
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
		ClientPlayNetworking.send(new FrequencyItemPacket(getInputFrequency(), hand));
	}
}
