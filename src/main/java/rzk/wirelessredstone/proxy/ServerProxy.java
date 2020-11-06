package rzk.wirelessredstone.proxy;

import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ServerProxy implements IProxy
{
	@Override
	public void clientSetup(FMLClientSetupEvent event) {}

	@Override
	public void openFrequencyGuiBlock(int frequency, BlockPos pos) {}

	@Override
	public void openFrequencyGuiItem(int frequency, Hand hand) {}
}
