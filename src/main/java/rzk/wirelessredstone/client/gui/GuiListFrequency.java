package rzk.wirelessredstone.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiListFrequency extends ExtendedList<GuiListFrequency.GuiListFrequencyEntry>
{
	public GuiListFrequency(Minecraft mc, int width, int height, int top, int bottom, int slotHeight)
	{
		super(mc, width, height, top, bottom, slotHeight);
	}

	@OnlyIn(Dist.CLIENT)
	public static class GuiListFrequencyEntry extends ExtendedList.AbstractListEntry<GuiListFrequencyEntry>
	{
		@Override
		public void render(int p_render_1_, int p_render_2_, int p_render_3_, int p_render_4_, int p_render_5_, int p_render_6_, int p_render_7_, boolean p_render_8_, float p_render_9_)
		{

		}
	}
}
