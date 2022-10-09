package rzk.wirelessredstone.generator.language;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public abstract class LanguageBase extends LanguageProvider
{
	// Language Keys

	// Item group
	public static final String ITEM_GROUP_WIRELESS_REDSTONE = "itemGroup.wirelessredstone";

	// Gui
	public static final String GUI_FREQUENCY_TITLE = "gui.wirelessredstone.frequency.title";
	public static final String GUI_CONFIG_TITLE = "gui.wirelessredstone.config.title";
	public static final String GUI_CONFIG_SIGNAL_STRENGTH = "gui.wirelessredstone.config.signal_strength";
	public static final String GUI_CONFIG_STRONG_POWER = "gui.wirelessredstone.config.strong_power";
	public static final String GUI_CONFIG_DISPLAY_RED = "gui.wirelessredstone.config.display.red";
	public static final String GUI_CONFIG_DISPLAY_GREEN = "gui.wirelessredstone.config.display.green";
	public static final String GUI_CONFIG_DISPLAY_BLUE = "gui.wirelessredstone.config.display.blue";
	public static final String GUI_CONFIG_HIGHLIGHT_RED = "gui.wirelessredstone.config.highlight.red";
	public static final String GUI_CONFIG_HIGHLIGHT_GREEN = "gui.wirelessredstone.config.highlight.green";
	public static final String GUI_CONFIG_HIGHLIGHT_BLUE = "gui.wirelessredstone.config.highlight.blue";
	public static final String GUI_CONFIG_HIGHLIGHT_TIME = "gui.wirelessredstone.config.highlight.time";

	// Tooltips
	public static final String ITEM_TOOLTIP_FREQUENCY = "item.wirelessredstone.tooltip.frequency";
	public static final String ITEM_TOOLTIP_STATE = "item.wirelessredstone.tooltip.state";
	public static final String ITEM_TOOLTIP_STATE_OFF = "item.wirelessredstone.tooltip.state.off";
	public static final String ITEM_TOOLTIP_STATE_ON = "item.wirelessredstone.tooltip.state.on";

	// Messages
	public static final String MESSAGE_TRANSMITTERS_EMPTY = "message.wirelessredstone.transmitters.empty";
	public static final String MESSAGE_TRANSMITTERS_ACTIVE = "message.wirelessredstone.transmitters.active";
	public static final String MESSAGE_TELEPORT = "message.wirelessredstone.teleport";
	public static final String MESSAGE_NO_FREQUENCY = "message.wirelessredstone.no_frequency";

	public LanguageBase(DataGenerator gen, String modid, String locale)
	{
		super(gen, modid, locale);
	}
}
