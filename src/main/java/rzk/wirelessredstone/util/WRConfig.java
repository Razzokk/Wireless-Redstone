package rzk.wirelessredstone.util;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import rzk.wirelessredstone.WirelessRedstone;

@Config(modid = WirelessRedstone.MOD_ID)
public class WRConfig
{
	@Comment("Color of the frequency text that is displayed on a transmitter/receiver in hex")
	@LangKey(LangKeys.CONFIG_FREQUENCY_COLOR)
	@RequiresMcRestart
	public static String freqDisplayColor = "0x000000";
}
