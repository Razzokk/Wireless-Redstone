package rzk.wirelessredstone.generators.language;

import net.minecraft.data.DataGenerator;
import rzk.wirelessredstone.registries.ModBlocks;
import rzk.wirelessredstone.registries.ModItems;

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
		addItem(ModItems.FREQUENCY_TOOL, "Frequenz Gerät");
		addItem(ModItems.FREQUENCY_SNIFFER, "Frequenz Schnüffler");

		// Misc
		add(ITEM_GROUP_WIRELESS_REDSTONE, "Wireless Redstone");

		// Gui
		add(GUI_FREQUENCY_TITLE, "Frequenz");

		// Item group
		add(ITEM_TOOLTIP_FREQUENCY, "Frequenz: (%s)");
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
