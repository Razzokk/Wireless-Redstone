package rzk.wirelessredstone.proxy;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import rzk.wirelessredstone.network.PacketFrequency;

public class ServerProxy implements IProxy
{
	@Override
	public void preInit(FMLPreInitializationEvent event) {}

	@Override
	public void openFrequencyGui(PacketFrequency packet) {}
}
