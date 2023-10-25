package rzk.wirelessredstone;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rzk.wirelessredstone.platform.Platform;

import java.util.ServiceLoader;

public final class WirelessRedstone
{
	public static final String MODID = "wirelessredstone";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final Platform PLATFORM = load();

	private WirelessRedstone() {}

	public static Identifier identifier(String path)
	{
		return new Identifier(MODID, path);
	}

	private static Platform load()
	{
		var platforms = ServiceLoader.load(Platform.class);
		return platforms.findFirst().orElseThrow(() -> new RuntimeException("Couldn't find wireless redstone platform!"));
	}
}
