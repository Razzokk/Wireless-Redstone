package rzk.wirelessredstone.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.ItemSniffer;

@Mod.EventBusSubscriber(modid = WirelessRedstone.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = {Dist.CLIENT})
public class HighlightRenderer
{
	@SubscribeEvent
	public static void onRenderWorldLast(RenderWorldLastEvent event)
	{
		PlayerEntity player = Minecraft.getInstance().player;
		ItemStack stack = player.getMainHandItem();

		if (!(stack.getItem() instanceof ItemSniffer))
			stack = player.getOffhandItem();

		if (stack.getItem() instanceof ItemSniffer && stack.hasTag())
		{
			int[] coords = stack.getOrCreateTag().getIntArray("highlight");

			if (coords.length > 0)
			{
				RenderSystem.lineWidth(2.5f);
				RenderSystem.disableDepthTest();
				RenderSystem.disableLighting();
				RenderSystem.disableTexture();

				IRenderTypeBuffer.Impl renderTypeBuffer = Minecraft.getInstance().renderBuffers().bufferSource();
				IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(RenderType.LINES);
				MatrixStack matrixStack = event.getMatrixStack();
				Vector3d cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

				float red = 1.0f;
				float green = 0.25f;
				float blue = 0.25f;

				for (int pos = 0; pos < coords.length; pos += 3)
				{
					matrixStack.pushPose();
					matrixStack.translate(cam.x() - coords[pos], cam.y() - coords[pos + 1], cam.z() - coords[pos + 2]);
					WorldRenderer.renderLineBox(matrixStack, vertexBuilder, coords[pos], coords[pos + 1], coords[pos + 2], coords[pos] + 1, coords[pos + 1] + 1, coords[pos + 2] + 1, red, green, blue, 1f);
					matrixStack.popPose();
				}

				renderTypeBuffer.endBatch(RenderType.LINES);
				RenderSystem.enableTexture();
				RenderSystem.enableLighting();
				RenderSystem.enableDepthTest();
				RenderSystem.lineWidth(1.0f);
			}
		}
	}
}
