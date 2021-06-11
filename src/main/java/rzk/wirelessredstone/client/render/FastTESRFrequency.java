package rzk.wirelessredstone.client.render;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.tile.TileFrequency;

@SideOnly(Side.CLIENT)
public class FastTESRFrequency extends FastTESR<TileFrequency>
{
	@Override
	public void renderTileEntityFast(TileFrequency tile, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5f, y + 0.5f, z + 0.5f);
		String s = String.valueOf(Short.toUnsignedInt(tile.getFrequency()));
		FontRenderer fontrenderer = getFontRenderer();
		setLightmapDisabled(true);

		for (int i = 0; i < 4; i++)
		{
			GlStateManager.pushMatrix();
			GlStateManager.rotate(i * 90, 0.0f, 1.0f, 0.0f);
			GlStateManager.translate(0.0f, 0.5f, 0.505f);
			GlStateManager.scale(1.0f / 96, -1.0f / 96, 1.0f / 96);
			fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 2, WirelessRedstone.freqColor);
			GlStateManager.popMatrix();
		}

		setLightmapDisabled(false);
		GlStateManager.popMatrix();
	}
}
