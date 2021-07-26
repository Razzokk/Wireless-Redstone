package rzk.wirelessredstone.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class SizedButton extends Button
{
	protected int textOffsetX;
	protected int textOffsetY;

	public SizedButton(int x, int y, int width, int height, Component component, int textOffsetX, int textOffsetY, OnPress onPress)
	{
		super(x, y, width, height, component, onPress);
		this.textOffsetX = textOffsetX;
		this.textOffsetY = textOffsetY;
	}

	public SizedButton(int x, int y, int width, int height, Component component, OnPress onPress)
	{
		this(x, y, width, height, component, 0, 0, onPress);
	}

	@Override
	public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		Minecraft minecraft = Minecraft.getInstance();
		Font font = minecraft.font;
		minecraft.getTextureManager().bindForSetup(WIDGETS_LOCATION);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
		int i = getYImage(isHovered());
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		blit(poseStack, x, y, 0, 46 + i * 20, width / 2, height / 2);
		blit(poseStack, x, y + height / 2, 0, 46 + i * 20 + 20 - height / 2, width / 2, height / 2);
		blit(poseStack, x + width / 2, y, 200 - width / 2, 46 + i * 20, width / 2, height / 2);
		blit(poseStack, x + width / 2, y + height / 2, 200 - width / 2, 46 + i * 20 + 20 - height / 2, width / 2, height / 2);
		renderBg(poseStack, minecraft, mouseX, mouseY);
		int j = getFGColor();
		drawCenteredString(poseStack, font, getMessage(), x + width / 2, y + (height - 8) / 2, j | (int) Math.ceil(alpha * 255.0F) << 24);

		if (isHovered())
			renderToolTip(poseStack, mouseX, mouseY);
	}
}
