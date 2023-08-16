package rzk.wirelessredstone.client.screen;

import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.ModNetworking;

@OnlyIn(Dist.CLIENT)
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
		ModNetworking.INSTANCE.sendToServer(new FrequencyItemPacket.SetFrequency(getInputFrequency(), hand));
	}
}
