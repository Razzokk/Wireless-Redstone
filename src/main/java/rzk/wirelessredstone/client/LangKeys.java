package rzk.wirelessredstone.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LangKeys
{
	@OnlyIn(Dist.CLIENT)
	public static class Block
	{
		public static final String TRANSMITTER = "block.wirelessredstone.wireless_transmitter";
		public static final String RECEIVER = "block.wirelessredstone.wireless_receiver";
	}

	@OnlyIn(Dist.CLIENT)
	public static class Item
	{
		public static final String CIRCUIT = "item.wirelessredstone.wireless_circuit";
		public static final String WIRELESS_REMOTE = "item.wirelessredstone.wireless_remote";
		public static final String FREQUENCY_COPIER = "item.wirelessredstone.frequency_copier";
	}

	@OnlyIn(Dist.CLIENT)
	public static class Gui
	{
		public static final String FREQUENCY = "gui.wirelessredstone.frequency";
		public static final String FREQUENCY_NAME = "gui.wirelessredstone.frequency_name";
		public static final String ADD = "gui.wirelessredstone.add";
		public static final String EXTEND = "gui.wirelessredstone.extend";
		public static final String REDUCE = "gui.wirelessredstone.reduce";
		public static final String SEARCHBAR = "gui.wirelessredstone.searchbar";
		public static final String DONE = "gui.done";
	}

	@OnlyIn(Dist.CLIENT)
	public static class Tooltip
	{
		public static final String FREQUENCY = "tooltip.wirelessredstone.frequency";
		public static final String STATE = "tooltip.wirelessredstone.state";
		public static final String ON = "tooltip.wirelessredstone.on";
		public static final String OFF = "tooltip.wirelessredstone.off";
	}

	@OnlyIn(Dist.CLIENT)
	public static class Misc
	{
		public static final String ITEM_GROUP_WIRELESS_REDSTONE = "itemGroup.wirelessredstone";
	}
}
