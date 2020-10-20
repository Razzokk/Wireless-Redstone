package rzk.wirelessredstone.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
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
		public void render(MatrixStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_)
		{

		}
	}
}
