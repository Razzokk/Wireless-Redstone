package rzk.wirelessredstone.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import rzk.wirelessredstone.blockentity.RedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.misc.Config;
import rzk.wirelessredstone.misc.Utils;

public class RedstoneTransceiverBER implements BlockEntityRenderer<RedstoneTransceiverBlockEntity>
{
	private final Font font;

	public RedstoneTransceiverBER(BlockEntityRendererProvider.Context ctx)
	{
		this.font = ctx.getFont();
	}

	@Override
	public void render(RedstoneTransceiverBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay)
	{
		int frequency = blockEntity.getFrequency();
		if (!Utils.isValidFrequency(frequency)) return;

		String str = String.valueOf(frequency);
		float textOffset = -font.width(str) / 2.0f;
		poseStack.translate(0.5, 1, 0.5);

		for (int i = 0; i < 4; i++)
		{
			poseStack.pushPose();
			poseStack.mulPose(new Quaternion(0, i * 90, 180, true));
			poseStack.translate(0, 0, -0.5078125);
			poseStack.scale(1.0f / 96, 1.0f / 96, 1.0f / 96);
			font.draw(poseStack, str, textOffset, 2.5f, Config.frequencyDisplayColor);
			poseStack.popPose();
		}
	}
}
