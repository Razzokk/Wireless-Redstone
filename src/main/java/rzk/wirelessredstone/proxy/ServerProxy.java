package rzk.wirelessredstone.proxy;

import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerProxy implements IProxy
{
	@Override
	public void clientSetup(FMLClientSetupEvent event) {}

	@Override
	public void openFrequencyGui(boolean isTransmitter, BlockPos pos) {}

	@Override
	public void openRemoteGui(int frequency, Hand hand) {}
}
