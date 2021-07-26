package rzk.wirelessredstone.util;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import rzk.wirelessredstone.WirelessRedstone;

public class WRConfig
{
	public static final String CATEGORY_CLIENT = "Client";
	public static final String CATEGORY_FREQ_DISPLAY_COLOR = "Frequency Display color";
	public static final String CATEGORY_HIGHLIGHT_COLOR = "Highlight color";
	public static final String CONFIG_PATH = "config/" + WirelessRedstone.MOD_ID  + ".toml";

	public static final ForgeConfigSpec CLIENT_CONFIG;

	public static final ForgeConfigSpec.IntValue FREQ_DISPLAY_COLOR_RED;
	public static final ForgeConfigSpec.IntValue FREQ_DISPLAY_COLOR_GREEN;
	public static final ForgeConfigSpec.IntValue FREQ_DISPLAY_COLOR_BLUE;
	public static final ForgeConfigSpec.IntValue HIGHLIGHT_COLOR_RED;
	public static final ForgeConfigSpec.IntValue HIGHLIGHT_COLOR_GREEN;
	public static final ForgeConfigSpec.IntValue HIGHLIGHT_COLOR_BLUE;
	public static final ForgeConfigSpec.IntValue SNIFFER_HIGHLIGHT_TIME_CONF;

	public static final int DISPLAY_COLOR_RED_DEFAULT = 0;
	public static final int DISPLAY_COLOR_GREEN_DEFAULT = 0;
	public static final int DISPLAY_COLOR_BLUE_DEFAULT = 0;
	public static final int HIGHLIGHT_COLOR_RED_DEFAULT = 255;
	public static final int HIGHLIGHT_COLOR_GREEN_DEFAULT = 63;
	public static final int HIGHLIGHT_COLOR_BLUE_DEFAULT = 63;
	public static final int SNIFFER_HIGHLIGHT_TIME_DEFAULT = 10;

	public static int freqDisplayColor = 0;
	public static int highlightColorRed = 0;
	public static int highlightColorGreen = 0;
	public static int highlightColorBlue = 0;
	public static int snifferHighlightTime = 0;

	static
	{
		ForgeConfigSpec.Builder clientBuilder = new ForgeConfigSpec.Builder();
		clientBuilder.comment("General configuration").push(CATEGORY_CLIENT);

		clientBuilder.push(CATEGORY_FREQ_DISPLAY_COLOR).comment("Color of the frequency text that is displayed on a transmitter/receiver");
		FREQ_DISPLAY_COLOR_RED = clientBuilder.defineInRange("red", DISPLAY_COLOR_RED_DEFAULT, 0, 255);
		FREQ_DISPLAY_COLOR_GREEN = clientBuilder.defineInRange("green", DISPLAY_COLOR_GREEN_DEFAULT, 0, 255);
		FREQ_DISPLAY_COLOR_BLUE = clientBuilder.defineInRange("blue", DISPLAY_COLOR_BLUE_DEFAULT, 0, 255);
		clientBuilder.pop();

		clientBuilder.push(CATEGORY_HIGHLIGHT_COLOR).comment("Color of the highlight outline using the sniffer for active transmitters");
		HIGHLIGHT_COLOR_RED = clientBuilder.defineInRange("red", HIGHLIGHT_COLOR_RED_DEFAULT, 0, 255);
		HIGHLIGHT_COLOR_GREEN = clientBuilder.defineInRange("green", HIGHLIGHT_COLOR_GREEN_DEFAULT, 0, 255);
		HIGHLIGHT_COLOR_BLUE = clientBuilder.defineInRange("blue", HIGHLIGHT_COLOR_BLUE_DEFAULT, 0, 255);
		clientBuilder.pop();

		SNIFFER_HIGHLIGHT_TIME_CONF = clientBuilder.comment("The time for the sniffer highlighting in seconds").defineInRange("snifferHighlightTime", SNIFFER_HIGHLIGHT_TIME_DEFAULT, 5, 500);
		clientBuilder.pop();

		CommentedFileConfig config = CommentedFileConfig.builder(CONFIG_PATH).sync().autoreload().writingMode(WritingMode.REPLACE).build();
		config.load();
		config.save();

		CLIENT_CONFIG = clientBuilder.build();
		CLIENT_CONFIG.setConfig(config);

		updateInternals();
	}

	public static void updateInternals()
	{
		freqDisplayColor = FREQ_DISPLAY_COLOR_BLUE.get() | (FREQ_DISPLAY_COLOR_GREEN.get() << 8) | (FREQ_DISPLAY_COLOR_RED.get() << 16);
		highlightColorRed = HIGHLIGHT_COLOR_RED.get();
		highlightColorGreen = HIGHLIGHT_COLOR_GREEN.get();
		highlightColorBlue = HIGHLIGHT_COLOR_BLUE.get();
		snifferHighlightTime = SNIFFER_HIGHLIGHT_TIME_CONF.get();
	}

	public static void save()
	{
		CLIENT_CONFIG.save();
		updateInternals();
	}

	public static void reset()
	{
		FREQ_DISPLAY_COLOR_RED.set(DISPLAY_COLOR_RED_DEFAULT);
		FREQ_DISPLAY_COLOR_GREEN.set(DISPLAY_COLOR_GREEN_DEFAULT);
		FREQ_DISPLAY_COLOR_BLUE.set(DISPLAY_COLOR_BLUE_DEFAULT);
		HIGHLIGHT_COLOR_RED.set(HIGHLIGHT_COLOR_RED_DEFAULT);
		HIGHLIGHT_COLOR_GREEN.set(HIGHLIGHT_COLOR_GREEN_DEFAULT);
		HIGHLIGHT_COLOR_BLUE.set(HIGHLIGHT_COLOR_BLUE_DEFAULT);
		SNIFFER_HIGHLIGHT_TIME_CONF.set(SNIFFER_HIGHLIGHT_TIME_DEFAULT);
	}
}
