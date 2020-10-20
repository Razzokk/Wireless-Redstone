package rzk.wirelessredstone.proxy;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rzk.wirelessredstone.packet.PacketFrequency;

public interface IProxy
{
	void clientSetup(FMLClientSetupEvent event);

	void openFrequencyGui(int frequency, PacketFrequency frequencyPacket);
}
