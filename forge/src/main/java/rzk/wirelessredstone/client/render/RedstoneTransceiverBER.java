package rzk.wirelessredstone.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import rzk.wirelessredstone.block.entity.RedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.misc.WRUtils;

public class RedstoneTransceiverBER<T extends RedstoneTransceiverBlockEntity> implements BlockEntityRenderer<T>
{
	private final Font font;

	public RedstoneTransceiverBER(BlockEntityRendererProvider.Context ctx)
	{
		font = ctx.getFont();
	}

	@Override
	public void render(T entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int overlay)
	{
		int frequency = entity.getFrequency();
		if (!WRUtils.isValidFrequency(frequency)) return;

		String str = String.valueOf(frequency);
		float textOffset = -font.width(str) / 2.0f;

		poseStack.pushPose();
		poseStack.translate(0.5, 1, 0.5);

		for (int i = 0; i < 4; i++)
		{
			poseStack.pushPose();
			poseStack.mulPose(Axis.YP.rotation((float) (i * Math.PI / 2)));
			poseStack.translate(0, 0, 0.5078125);
			poseStack.scale(1f / 96, -1f / 96, 1f / 96);
			font.drawInBatch(str, textOffset, 2.5f, WRConfig.frequencyDisplayColor, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.POLYGON_OFFSET, 0, 0xFFFFFF);
			poseStack.popPose();
		}

		poseStack.popPose();
	}
}
