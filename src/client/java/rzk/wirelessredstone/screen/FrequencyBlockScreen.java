package rzk.wirelessredstone.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.network.FrequencyBlockPacket;

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
		ClientPlayNetworking.send(new FrequencyBlockPacket(getInputFrequency(), pos));
	}
}
