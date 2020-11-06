package rzk.wirelessredstone.proxy;

import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rzk.wirelessredstone.packet.PacketFrequency;

public interface IProxy
{
	void clientSetup(FMLClientSetupEvent event);

	void openFrequencyGuiBlock(int frequency, BlockPos pos);

	void openFrequencyGuiItem(int frequency, Hand hand);
}
