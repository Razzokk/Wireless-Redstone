package rzk.wirelessredstone.generator.language;

import net.minecraft.data.PackOutput;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

public class LanguageEN extends LanguageBase
{
	public LanguageEN(PackOutput packOutput)
	{
		super(packOutput, "en_us");
	}

	@Override
	protected void addTranslations()
	{
		// Blocks
		addBlock(ModBlocks.REDSTONE_TRANSMITTER, "Redstone Transmitter");
		addBlock(ModBlocks.REDSTONE_RECEIVER, "Redstone Receiver");

		// Items
		addItem(ModItems.CIRCUIT, "Circuit");
		addItem(ModItems.FREQUENCY_TOOL, "Frequency Tool");
		addItem(ModItems.FREQUENCY_SNIFFER, "Frequency Sniffer");

		// Misc
		add(ITEM_GROUP_WIRELESS_REDSTONE, "Wireless Redstone");

		// Gui
		add(GUI_FREQUENCY_TITLE, "Frequency");
		add(GUI_CONFIG_TITLE, "Wireless Redstone Config");
		add(GUI_CONFIG_SIGNAL_STRENGTH, "Signal strength");
		add(GUI_CONFIG_STRONG_POWER, "Strong power");
		add(GUI_CONFIG_DISPLAY_RED, "Display red");
		add(GUI_CONFIG_DISPLAY_GREEN, "Display green");
		add(GUI_CONFIG_DISPLAY_BLUE, "Display blue");
		add(GUI_CONFIG_HIGHLIGHT_RED, "Highlight red");
		add(GUI_CONFIG_HIGHLIGHT_GREEN, "Highlight green");
		add(GUI_CONFIG_HIGHLIGHT_BLUE, "Highlight blue");
		add(GUI_CONFIG_HIGHLIGHT_TIME, "Highlight time");

		// Item group
		add(ITEM_TOOLTIP_FREQUENCY, "Frequency: %s");
		add(ITEM_TOOLTIP_STATE, "State: %s");
		add(ITEM_TOOLTIP_STATE_OFF, "Off");
		add(ITEM_TOOLTIP_STATE_ON, "On");

		// Messages
		add(MESSAGE_TRANSMITTERS_EMPTY, "There are no active transmitters on this frequency (%s)");
		add(MESSAGE_TRANSMITTERS_ACTIVE, "Active transmitters on this frequency (%s): %s");
		add(MESSAGE_TELEPORT, "Teleport here");
		add(MESSAGE_NO_FREQUENCY, "No frequency set");
	}
}
