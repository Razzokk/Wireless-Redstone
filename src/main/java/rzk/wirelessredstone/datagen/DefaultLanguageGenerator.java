package rzk.wirelessredstone.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import rzk.wirelessredstone.block.ModBlocks;
import rzk.wirelessredstone.item.ModItems;

public class DefaultLanguageGenerator extends FabricLanguageProvider
{
	// Translation Keys

	// Item group
	public static final String ITEM_GROUP_WIRELESS_REDSTONE = "item_group.wirelessredstone.wirelessredstone";

	// GUI
	public static final String GUI_FREQUENCY_TITLE = "gui.wirelessredstone.frequency.title";
	public static final String GUI_CONFIG_TITLE = "gui.wirelessredstone.config.title";
	public static final String GUI_CONFIG_CATEGORY_GENERAL = "gui.wirelessredstone.config.category.general";
	public static final String GUI_CONFIG_CATEGORY_CLIENT = "gui.wirelessredstone.config.category.client";
	public static final String GUI_CONFIG_SIGNAL_STRENGTH = "gui.wirelessredstone.config.signal_strength";
	public static final String GUI_CONFIG_STRONG_POWER = "gui.wirelessredstone.config.strong_power";
	public static final String GUI_CONFIG_DISPLAY_COLOR = "gui.wirelessredstone.config.display.color";
	public static final String GUI_CONFIG_HIGHLIGHT_COLOR = "gui.wirelessredstone.config.highlight.color";
	public static final String GUI_CONFIG_HIGHLIGHT_TIME = "gui.wirelessredstone.config.highlight.time";

	// Tooltips
	public static final String TOOLTIP_FREQUENCY = "item.wirelessredstone.tooltip.frequency";
	public static final String TOOLTIP_STATE = "item.wirelessredstone.tooltip.state";
	public static final String TOOLTIP_STATE_OFF = "item.wirelessredstone.tooltip.state.off";
	public static final String TOOLTIP_STATE_ON = "item.wirelessredstone.tooltip.state.on";
	public static final String TOOLTIP_MODE = "item.wirelessredstone.tooltip.mode";
	public static final String TOOLTIP_MODE_TOGGLE = "item.wirelessredstone.tooltip.mode.toggle";
	public static final String TOOLTIP_MODE_SWITCH = "item.wirelessredstone.tooltip.mode.switch";

	// Messages
	public static final String MESSAGE_TRANSMITTERS_EMPTY = "message.wirelessredstone.transmitters.empty";
	public static final String MESSAGE_TRANSMITTERS_ACTIVE = "message.wirelessredstone.transmitters.active";
	public static final String MESSAGE_TELEPORT = "message.wirelessredstone.teleport";
	public static final String MESSAGE_NO_FREQUENCY = "message.wirelessredstone.no_frequency";

	protected DefaultLanguageGenerator(FabricDataOutput dataOutput)
	{
		super(dataOutput);
	}

	@Override
	public void generateTranslations(TranslationBuilder translations)
	{
		// Blocks
		translations.add(ModBlocks.REDSTONE_TRANSMITTER, "Redstone Transmitter");
		translations.add(ModBlocks.REDSTONE_RECEIVER, "Redstone Receiver");

		// Items
		translations.add(ModItems.CIRCUIT, "Circuit");
		translations.add(ModItems.FREQUENCY_TOOL, "Frequency Tool");
		translations.add(ModItems.FREQUENCY_SNIFFER, "Frequency Sniffer");
		translations.add(ModItems.REMOTE, "Remote");

		// Other
		translations.add(ITEM_GROUP_WIRELESS_REDSTONE, "Wireless Redstone");
		translations.add(GUI_FREQUENCY_TITLE, "Frequency");
		translations.add(GUI_CONFIG_TITLE, "Wireless Redstone Config");
		translations.add(GUI_CONFIG_CATEGORY_GENERAL, "General");
		translations.add(GUI_CONFIG_CATEGORY_CLIENT, "Client");
		translations.add(GUI_CONFIG_SIGNAL_STRENGTH, "Signal strength");
		translations.add(GUI_CONFIG_STRONG_POWER, "Strong power");
		translations.add(GUI_CONFIG_DISPLAY_COLOR, "Display color");
		translations.add(GUI_CONFIG_HIGHLIGHT_COLOR, "Highlight color");
		translations.add(GUI_CONFIG_HIGHLIGHT_TIME, "Highlight time");
		translations.add(TOOLTIP_FREQUENCY, "Frequency: %s");
		translations.add(TOOLTIP_STATE, "State: %s");
		translations.add(TOOLTIP_STATE_OFF, "Off");
		translations.add(TOOLTIP_STATE_ON, "On");
		translations.add(TOOLTIP_MODE, "Mode: %s");
		translations.add(TOOLTIP_MODE_TOGGLE, "Toggle");
		translations.add(TOOLTIP_MODE_SWITCH, "Switch");
		translations.add(MESSAGE_TRANSMITTERS_EMPTY, "There are no active transmitters on this frequency (%s)");
		translations.add(MESSAGE_TRANSMITTERS_ACTIVE, "Active transmitters on this frequency (%s): %s");
		translations.add(MESSAGE_TELEPORT, "Teleport here");
		translations.add(MESSAGE_NO_FREQUENCY, "No frequency set");
	}
}
