package rzk.wirelessredstone.misc;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config
{
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final ForgeConfigSpec CLIENT_SPEC;

    // Common

    private static final String CATEGORY_COMMON = "Common";

    public static final ForgeConfigSpec.IntValue REDSTONE_RECEIVER_SIGNAL_STRENGTH;
    public static final ForgeConfigSpec.BooleanValue REDSTONE_RECEIVER_STRONG_POWER;

    public static int redstoneReceiverSignalStrength;
    public static boolean redstoneReceiverStrongPower;

    // Client

    private static final String CATEGORY_CLIENT = "Client";
    private static final String CATEGORY_FREQ_DISPLAY_COLOR = "Frequency Display color";
    private static final String CATEGORY_HIGHLIGHT_COLOR = "Highlight color";

    public static final ForgeConfigSpec.IntValue FREQUENCY_DISPLAY_COLOR_RED;
    public static final ForgeConfigSpec.IntValue FREQUENCY_DISPLAY_COLOR_GREEN;
    public static final ForgeConfigSpec.IntValue FREQUENCY_DISPLAY_COLOR_BLUE;
    public static final ForgeConfigSpec.IntValue HIGHLIGHT_COLOR_RED;
    public static final ForgeConfigSpec.IntValue HIGHLIGHT_COLOR_GREEN;
    public static final ForgeConfigSpec.IntValue HIGHLIGHT_COLOR_BLUE;
    public static final ForgeConfigSpec.IntValue SNIFFER_HIGHLIGHT_TIME;

    public static int frequencyDisplayColor;
    public static int highlightColorRed;
    public static int highlightColorGreen;
    public static int highlightColorBlue;
    public static int snifferHighlightTime;

    static
    {
        // Common

        ForgeConfigSpec.Builder commonBuilder = new ForgeConfigSpec.Builder();
        commonBuilder.comment("General configuration").push(CATEGORY_COMMON);

        REDSTONE_RECEIVER_SIGNAL_STRENGTH = commonBuilder.comment("Signal strength of redstone receivers").defineInRange("redstoneReceiverSignalStrength", 15, 1, 15);
        REDSTONE_RECEIVER_STRONG_POWER = commonBuilder.comment("Should redstone receivers provider strong power?").define("redstoneReceiverStrongPower", true);

        commonBuilder.pop();
        COMMON_SPEC = commonBuilder.build();

        // Client

        ForgeConfigSpec.Builder clientBuilder = new ForgeConfigSpec.Builder();
        clientBuilder.comment("Client configuration").push(CATEGORY_CLIENT);

        clientBuilder.push(CATEGORY_FREQ_DISPLAY_COLOR).comment("Color of the frequency text that is displayed on a transmitter/receiver");
        FREQUENCY_DISPLAY_COLOR_RED = clientBuilder.defineInRange("red", 0, 0, 255);
        FREQUENCY_DISPLAY_COLOR_GREEN = clientBuilder.defineInRange("green", 0, 0, 255);
        FREQUENCY_DISPLAY_COLOR_BLUE = clientBuilder.defineInRange("blue", 0, 0, 255);
        clientBuilder.pop();

        clientBuilder.push(CATEGORY_HIGHLIGHT_COLOR).comment("Color of the highlight outline using the sniffer for active transmitters");
        HIGHLIGHT_COLOR_RED = clientBuilder.defineInRange("red", 255, 0, 255);
        HIGHLIGHT_COLOR_GREEN = clientBuilder.defineInRange("green", 63, 0, 255);
        HIGHLIGHT_COLOR_BLUE = clientBuilder.defineInRange("blue", 63, 0, 255);
        clientBuilder.pop();

        SNIFFER_HIGHLIGHT_TIME = clientBuilder.comment("The time for the sniffer highlighting in seconds").defineInRange("snifferHighlightTime", 10, 1, 500);

        clientBuilder.pop();
        CLIENT_SPEC = clientBuilder.build();
    }

    public static void updateInternals()
    {
        // Common

        redstoneReceiverSignalStrength = REDSTONE_RECEIVER_SIGNAL_STRENGTH.get();
        redstoneReceiverStrongPower = REDSTONE_RECEIVER_STRONG_POWER.get();

        // Client

        frequencyDisplayColor = FREQUENCY_DISPLAY_COLOR_BLUE.get() | (FREQUENCY_DISPLAY_COLOR_GREEN.get() << 8) | (FREQUENCY_DISPLAY_COLOR_RED.get() << 16);
        highlightColorRed = HIGHLIGHT_COLOR_RED.get();
        highlightColorGreen = HIGHLIGHT_COLOR_GREEN.get();
        highlightColorBlue = HIGHLIGHT_COLOR_BLUE.get();
        snifferHighlightTime = SNIFFER_HIGHLIGHT_TIME.get();
    }
}
