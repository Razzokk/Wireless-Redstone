package rzk.wirelessredstone.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.client.ClientSubscriber;
import rzk.wirelessredstone.item.ItemSniffer;

@Mod.EventBusSubscriber(modid = WirelessRedstone.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = {Dist.CLIENT})
public class HighlightRenderer
{
	@SubscribeEvent
	public static void renderSnifferHighlights(RenderWorldLastEvent event)
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
				IRenderTypeBuffer.Impl renderTypeBuffer = Minecraft.getInstance().renderBuffers().bufferSource();
				Vector3d cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
				IVertexBuilder vertexBuilder = renderTypeBuffer.getBuffer(ClientSubscriber.LINES);
				MatrixStack matrixStack = event.getMatrixStack();
				matrixStack.pushPose();
				matrixStack.translate(-cam.x(), -cam.y(),  -cam.z());

				for (int pos = 0; pos < coords.length; pos += 3)
				{
					int x = coords[pos];
					int y = coords[pos + 1];
					int z = coords[pos + 2];
					player.shouldRender(x, y, z);
					WorldRenderer.renderLineBox(matrixStack, vertexBuilder, x, y, z, x + 1, y + 1, z + 1, 1.0f, 0.25f, 0.25f, 1f);
				}

				matrixStack.popPose();
				RenderSystem.disableDepthTest();
				RenderSystem.depthMask(false);
				renderTypeBuffer.endBatch(ClientSubscriber.LINES);
				RenderSystem.enableDepthTest();
				RenderSystem.depthMask(true);
			}
		}
	}
}
