package rzk.wirelessredstone.generators.language;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public abstract class LanguageBase extends LanguageProvider
{
	// Language Keys

	// Item group
	public static final String ITEM_GROUP_WIRELESS_REDSTONE = "itemGroup.wirelessredstone";

	// Gui
	public static final String GUI_FREQUENCY_TITLE = "gui.wirelessredstone.frequency.title";

	// Tooltips
	public static final String ITEM_TOOLTIP_FREQUENCY = "item.wirelessredstone.tooltip.frequency";
	public static final String ITEM_TOOLTIP_STATE = "item.wirelessredstone.tooltip.state";
	public static final String ITEM_TOOLTIP_STATE_OFF = "item.wirelessredstone.tooltip.state.off";
	public static final String ITEM_TOOLTIP_STATE_ON = "item.wirelessredstone.tooltip.state.on";

	// Messages
	public static final String MESSAGE_TRANSMITTERS_EMPTY = "message.wirelessredstone.transmitters.empty";
	public static final String MESSAGE_TRANSMITTERS_ACTIVE = "message.wirelessredstone.transmitters.active";
	public static final String MESSAGE_TELEPORT = "message.wirelessredstone.teleport";


	public LanguageBase(DataGenerator gen, String modid, String locale)
	{
		super(gen, modid, locale);
	}
}
