package rzk.wirelessredstone.util;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import rzk.wirelessredstone.WirelessRedstone;

@Config(modid = WirelessRedstone.MOD_ID)
public class WRConfig
{
	@Comment("Color of the frequency text that is displayed on a transmitter/receiver in hex")
	@LangKey(LangKeys.CONFIG_FREQUENCY_COLOR)
	@RequiresMcRestart
	public static String freqDisplayColor = "0x000000";

	@Comment("The time for the sniffer highlighting in seconds")
	@LangKey(LangKeys.CONFIG_SNIFFER_HIGHLIGHT_TIME)
	@RangeInt(min = 1)
	public static int snifferHighlightTime = 5;
}
