package rzk.wirelessredstone.proxy;

import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public interface IProxy
{
	void clientSetup(FMLClientSetupEvent event);

	void openFrequencyGui(boolean isTransmitter, BlockPos pos);

	void openRemoteGui(int frequency, Hand hand);
}
