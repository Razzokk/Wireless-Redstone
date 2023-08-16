package rzk.wirelessredstone.client.screen;

import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.ModNetworking;

@OnlyIn(Dist.CLIENT)
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
		ModNetworking.INSTANCE.sendToServer(new FrequencyBlockPacket.SetFrequency(getInputFrequency(), pos));
	}
}
