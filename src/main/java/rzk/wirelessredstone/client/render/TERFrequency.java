package rzk.wirelessredstone.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Quaternion;
import rzk.wirelessredstone.tile.TileFrequency;
import rzk.wirelessredstone.util.WRConfig;

public class TERFrequency extends TileEntityRenderer<TileFrequency>
{
	public TERFrequency(TileEntityRendererDispatcher rendererDispatcher)
	{
		super(rendererDispatcher);
	}

	@Override
	public void render(TileFrequency tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
	{
		FontRenderer fontRenderer = renderer.getFont();
		String s = String.valueOf(Short.toUnsignedInt(tile.getFrequency()));
		float textOffset = -fontRenderer.width(s) / 2.0f;
		matrixStack.translate(0.5, 1, 0.5);

		for (int i = 0; i < 4; i++)
		{
			matrixStack.pushPose();
			matrixStack.mulPose(new Quaternion(0, i * 90, 180, true));
			matrixStack.translate(0, 0, -0.5078125);
			matrixStack.scale(1.0f / 96, 1.0f / 96, 1.0f / 96);
			fontRenderer.draw(matrixStack, s, textOffset, 2.5f, WRConfig.freqDisplayColor);
			matrixStack.popPose();
		}
	}
}
