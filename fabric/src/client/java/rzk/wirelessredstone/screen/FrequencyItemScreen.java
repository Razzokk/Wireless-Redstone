package rzk.wirelessredstone.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Hand;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.networking.ModClientNetworking;

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
		ModClientNetworking.send(new FrequencyItemPacket(getInputFrequency(), hand));
	}
}
