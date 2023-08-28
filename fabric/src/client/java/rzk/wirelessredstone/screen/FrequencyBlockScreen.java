package rzk.wirelessredstone.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.networking.ModClientNetworking;

@Environment(EnvType.CLIENT)
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
		ModClientNetworking.send(new FrequencyBlockPacket(getInputFrequency(), pos));
	}
}
