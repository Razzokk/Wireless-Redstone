package rzk.wirelessredstone;

import rzk.wirelessredstone.platform.ClientPlatform;

import java.util.ServiceLoader;

public class WirelessRedstoneClient
{
	public static final ClientPlatform PLATFORM = load();

	private static ClientPlatform load()
	{
		var platforms = ServiceLoader.load(ClientPlatform.class);
		return platforms.findFirst().orElseThrow(() -> new RuntimeException("Couldn't find wireless redstone client platform!"));
	}
}
