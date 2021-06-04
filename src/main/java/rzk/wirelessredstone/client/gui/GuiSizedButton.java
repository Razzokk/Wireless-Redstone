package rzk.wirelessredstone.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSizedButton extends GuiButton
{
	protected int textOffsetX;
	protected int textOffsetY;

	public GuiSizedButton(int id, int x, int y, int width, int height, String buttonText, int textOffsetX, int textOffsetY)
	{
		super(id, x, y, width, height, buttonText);
		this.textOffsetX = textOffsetX;
		this.textOffsetY = textOffsetY;
	}

	public GuiSizedButton(int id, int x, int y, int width, int height, String buttonText)
	{
		this(id, x, y, width, height, buttonText, 0, 0);
	}

	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		if (visible)
		{
			FontRenderer fontrenderer = mc.fontRenderer;
			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
			hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
			int i = getHoverState(hovered);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			drawTexturedModalRect(x, y, 0, 46 + i * 20, width / 2, height / 2);
			drawTexturedModalRect(x, y + height / 2, 0, 46 + i * 20 + 20 - height / 2, width / 2, height / 2);
			drawTexturedModalRect(x + width / 2, y, 200 - width / 2, 46 + i * 20, width / 2, height / 2);
			drawTexturedModalRect(x + width / 2, y + height / 2, 200 - width / 2, 46 + i * 20 + 20 - height / 2, width / 2, height / 2);
			mouseDragged(mc, mouseX, mouseY);

			int j = 14737632;

			if (packedFGColour != 0)
				j = packedFGColour;
			else if (!enabled)
				j = 10526880;
			else if (hovered)
				j = 16777120;

			drawCenteredString(fontrenderer, displayString, x + width / 2 + textOffsetX, y + (height - 8) / 2 + textOffsetY, j);
		}
	}
}
