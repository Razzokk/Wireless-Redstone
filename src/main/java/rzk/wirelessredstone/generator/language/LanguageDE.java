package rzk.wirelessredstone.generator.language;

import net.minecraft.data.DataGenerator;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

public class LanguageDE extends LanguageBase
{
	public LanguageDE(DataGenerator gen, String modid)
	{
		super(gen, modid, "de_de");
	}

	@Override
	protected void addTranslations()
	{
		// Blocks
		addBlock(ModBlocks.REDSTONE_TRANSMITTER, "Redstone Sender");
		addBlock(ModBlocks.REDSTONE_RECEIVER, "Redstone Empfänger");

		// Items
		addItem(ModItems.CIRCUIT, "Schaltkreis");
		addItem(ModItems.FREQUENCY_TOOL, "Frequenzgerät");
		addItem(ModItems.FREQUENCY_SNIFFER, "Frequenzfühler");

		// Misc
		add(ITEM_GROUP_WIRELESS_REDSTONE, "Wireless Redstone");

		// Gui
		add(GUI_FREQUENCY_TITLE, "Frequenz");
		add(GUI_CONFIG_TITLE, "Wireless Redstone Config");
		add(GUI_CONFIG_SIGNAL_STRENGTH, "Signalstärke");
		add(GUI_CONFIG_STRONG_POWER, "Strong power");
		add(GUI_CONFIG_DISPLAY_RED, "Anzeige Rot");
		add(GUI_CONFIG_DISPLAY_GREEN, "Anzeige Grün");
		add(GUI_CONFIG_DISPLAY_BLUE, "Anzeige Blau");
		add(GUI_CONFIG_HIGHLIGHT_RED, "Aufleuchten Rot");
		add(GUI_CONFIG_HIGHLIGHT_GREEN, "Aufleuchten Grün");
		add(GUI_CONFIG_HIGHLIGHT_BLUE, "Aufleuchten Blau");
		add(GUI_CONFIG_HIGHLIGHT_TIME, "Aufleuchtzeit");

		// Item group
		add(ITEM_TOOLTIP_FREQUENCY, "Frequenz: %s");
		add(ITEM_TOOLTIP_STATE, "Status: %s");
		add(ITEM_TOOLTIP_STATE_OFF, "Aus");
		add(ITEM_TOOLTIP_STATE_ON, "An");

		// Messages
		add(MESSAGE_TRANSMITTERS_EMPTY, "Keine aktiven Sender auf dieser Frequenz (%s)");
		add(MESSAGE_TRANSMITTERS_ACTIVE, "Aktive Sender auf dieser Frequenz (%s): %s");
		add(MESSAGE_TELEPORT, "Hierher teleportieren");
		add(MESSAGE_NO_FREQUENCY, "Keine Frequenz gesetzt");
	}
}
