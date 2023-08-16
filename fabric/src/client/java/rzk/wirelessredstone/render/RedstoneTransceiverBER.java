package rzk.wirelessredstone.render;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import rzk.wirelessredstone.block.entity.RedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.misc.WRUtils;

public class RedstoneTransceiverBER<T extends RedstoneTransceiverBlockEntity> implements BlockEntityRenderer<T>
{
	private final TextRenderer textRenderer;

	public RedstoneTransceiverBER(BlockEntityRendererFactory.Context ctx)
	{
		textRenderer = ctx.getTextRenderer();
	}

	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		int frequency = entity.getFrequency();
		if (!WRUtils.isValidFrequency(frequency)) return;

		String str = String.valueOf(frequency);
		float textOffset = -textRenderer.getWidth(str) / 2.0f;

		matrices.push();
		matrices.translate(0.5, 1, 0.5);

		for (int i = 0; i < 4; i++)
		{
			matrices.push();
			matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) (i * Math.PI / 2)));
			matrices.translate(0, 0, 0.5078125);
			matrices.scale(1f / 96, -1f / 96, 1f / 96);
			textRenderer.draw(str, textOffset, 2.5f, WRConfig.frequencyDisplayColor, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, 0xFFFFFF);
			matrices.pop();
		}

		matrices.pop();
	}
}
