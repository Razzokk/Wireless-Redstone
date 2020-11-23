package rzk.wirelessredstone.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import rzk.wirelessredstone.tile.TileFrequency;

public class TERFrequency extends TileEntityRenderer<TileFrequency>
{
	public TERFrequency(TileEntityRendererDispatcher rendererDispatcher)
	{
		super(rendererDispatcher);
	}

	@Override
	public void render(TileFrequency tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
	{
		FontRenderer fontrenderer = this.renderDispatcher.getFontRenderer();
		String s = String.valueOf(tile.getFrequency());
		float textOffset = (float) (-fontrenderer.getStringWidth(s) / 2);

		matrixStack.translate(0.5, 0.5, 0.5);

		for (int i = 0; i < 4; i++)
		{
			matrixStack.push();
			matrixStack.rotate(new Quaternion(0, i * 90, 180, true));
			matrixStack.translate(0, -0.5, -0.51);
			matrixStack.scale(1f / 96, 1f / 96, 1f / 96);
			fontrenderer.renderString(s, textOffset, 2, 0, false, matrixStack.getLast().getMatrix(), buffer, false, 0, combinedLight);
			matrixStack.pop();
		}
	}
}
