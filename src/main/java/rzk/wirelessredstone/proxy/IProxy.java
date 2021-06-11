package rzk.wirelessredstone.proxy;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import rzk.wirelessredstone.network.PacketFrequency;

public interface IProxy
{
	void preInit(FMLPreInitializationEvent event);

	void openFrequencyGui(PacketFrequency packet);
}
