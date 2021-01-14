package rzk.wirelessredstone.client.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.model.animation.FastTESR;
import rzk.wirelessredstone.tile.TileFrequency;

public class FastTESRFrequency extends FastTESR<TileFrequency>
{
	@Override
	public void renderTileEntityFast(TileFrequency tile, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5f, y + 0.5f, z + 0.5f);
		String s = String.valueOf(tile.getFrequency());
		FontRenderer fontrenderer = getFontRenderer();

		for (int i = 0; i < 4; i++)
		{
			GlStateManager.pushMatrix();
			GlStateManager.rotate(i * 90, 0f, 1f, 0f);
			GlStateManager.translate(0f, 0.5f, 0.515625f);
			GlStateManager.scale(1f / 96, -1f / 96, 1f / 96);
			fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 2, 0);
			GlStateManager.popMatrix();
		}

		GlStateManager.popMatrix();
	}
}
