package rzk.wirelessredstone.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class SizedButton extends Button
{
	protected int textOffsetX;
	protected int textOffsetY;

	public SizedButton(int x, int y, int width, int height, ITextComponent component, int textOffsetX, int textOffsetY, IPressable pressable)
	{
		super(x, y, width, height, component, pressable);
		this.textOffsetX = textOffsetX;
		this.textOffsetY = textOffsetY;
	}

	public SizedButton(int x, int y, int width, int height, ITextComponent component, IPressable pressable)
	{
		this(x, y, width, height, component, 0, 0, pressable);
	}

	@Override
	public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		Minecraft minecraft = Minecraft.getInstance();
		FontRenderer fontrenderer = minecraft.font;
		minecraft.getTextureManager().bind(WIDGETS_LOCATION);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
		int i = getYImage(isHovered());
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		blit(matrixStack, x, y, 0, 46 + i * 20, width / 2, height / 2);
		blit(matrixStack, x, y + height / 2, 0, 46 + i * 20 + 20 - height / 2, width / 2, height / 2);
		blit(matrixStack, x + width / 2, y, 200 - width / 2, 46 + i * 20, width / 2, height / 2);
		blit(matrixStack, x + width / 2, y + height / 2, 200 - width / 2, 46 + i * 20 + 20 - height / 2, width / 2, height / 2);
		renderBg(matrixStack, minecraft, mouseX, mouseY);
		int j = getFGColor();
		drawCenteredString(matrixStack, fontrenderer, getMessage(), x + width / 2, y + (height - 8) / 2, j | MathHelper.ceil(alpha * 255.0F) << 24);

		if (isHovered())
			renderToolTip(matrixStack, mouseX, mouseY);
	}
}
