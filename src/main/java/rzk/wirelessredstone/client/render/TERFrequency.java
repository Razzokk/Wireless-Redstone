package rzk.wirelessredstone.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import rzk.wirelessredstone.tile.TileFrequency;
import rzk.wirelessredstone.util.WRConfig;

public class TERFrequency implements BlockEntityRenderer<TileFrequency>
{
	private final Font font;

	public TERFrequency(BlockEntityRendererProvider.Context context)
	{
		font = context.getFont();
	}

	@Override
	public void render(TileFrequency tile, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int combinedLight, int combinedOverlay)
	{
		String s = String.valueOf(Short.toUnsignedInt(tile.getFrequency()));
		float textOffset = -font.width(s) / 2.0f;
		poseStack.translate(0.5, 1, 0.5);

		for (int i = 0; i < 4; i++)
		{
			poseStack.pushPose();
			poseStack.mulPose(new Quaternion(0, i * 90, 180, true));
			poseStack.translate(0, 0, -0.5078125);
			poseStack.scale(1.0f / 96, 1.0f / 96, 1.0f / 96);
			font.draw(poseStack, s, textOffset, 2.5f, WRConfig.freqDisplayColor);
			poseStack.popPose();
		}
	}
}
