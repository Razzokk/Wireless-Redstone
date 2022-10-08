package rzk.wirelessredstone.generators.language;

import net.minecraft.data.DataGenerator;
import rzk.wirelessredstone.registries.ModBlocks;
import rzk.wirelessredstone.registries.ModItems;

public class LanguageEN extends LanguageBase
{
	public LanguageEN(DataGenerator gen, String modid)
	{
		super(gen, modid, "en_us");
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

		// Item group
		add(ITEM_TOOLTIP_FREQUENCY, "Frequency: (%s)");
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
