package rzk.wirelessredstone.proxy;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rzk.wirelessredstone.packet.PacketFrequency;

@OnlyIn(Dist.DEDICATED_SERVER)
public class ServerProxy implements IProxy
{
	@Override
	public void clientSetup(FMLClientSetupEvent event) {}

	@Override
	public void openFrequencyGui(int frequency, PacketFrequency frequencyPacket) {}
}
