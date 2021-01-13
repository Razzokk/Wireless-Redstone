package rzk.wirelessredstone.client.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.util.vector.Quaternion;
import rzk.wirelessredstone.tile.TileFrequency;

public class TESRFrequency extends TileEntitySpecialRenderer<TileFrequency>
{
	@Override
	public void render(TileFrequency tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
	{
		super.render(tile, x, y, z, partialTicks, destroyStage, alpha);

		FontRenderer fontrenderer = rendererDispatcher.getFontRenderer();
		String s = String.valueOf(tile.getFrequency());
		float textOffset = (float) (-fontrenderer.getStringWidth(s) / 2);

		GlStateManager.translate(0.5, 0.5, 0.5);

		for (int i = 0; i < 4; i++)
		{
			GlStateManager.pushMatrix();
			GlStateManager.rotate(new Quaternion(0, i * 90, 180, true));
			GlStateManager.translate(0, -0.5, -0.51);
			GlStateManager.scale(1f / 96, 1f / 96, 1f / 96);
			fontrenderer.renderString(s, textOffset, 2, 0, false, matrixStack.getLast().getMatrix(), buffer, false, 0, combinedLight);
			GlStateManager.popMatrix();
		}
	}
}
