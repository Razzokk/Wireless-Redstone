package rzk.wirelessredstone.client.gui;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rzk.wirelessredstone.rsnetwork.Channel;

@SideOnly(Side.CLIENT)
public class GuiListChannel extends GuiListExtended
{
	private final ObjectList<Channel> channels;

	public GuiListChannel(Minecraft mc, int width, int height, int top, int bottom, int slotHeight)
	{
		super(mc, width, height, top, bottom, slotHeight);
		channels = new ObjectArrayList<>();
	}

	@Override
	public IGuiListEntry getListEntry(int index)
	{
		return new ChannelEntry(channels.get(index));
	}

	@Override
	protected int getSize()
	{
		return channels.size();
	}

	private class ChannelEntry implements IGuiListEntry
	{
		private final Channel channel;

		private ChannelEntry(Channel channel)
		{
			this.channel = channel;
		}

		public Channel getChannel()
		{
			return channel;
		}

		@Override
		public void updatePosition(int slotIndex, int x, int y, float partialTicks)
		{

		}

		@Override
		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks)
		{

		}

		@Override
		public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY)
		{
			return false;
		}

		@Override
		public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
		{

		}
	}
}
