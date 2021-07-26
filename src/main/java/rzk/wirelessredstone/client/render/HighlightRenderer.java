package rzk.wirelessredstone.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.ItemSniffer;
import rzk.wirelessredstone.util.WRConfig;

@Mod.EventBusSubscriber(modid = WirelessRedstone.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = {Dist.CLIENT})
public class HighlightRenderer
{
	@SubscribeEvent
	public static void renderSnifferHighlights(RenderWorldLastEvent event)
	{
		Player player = Minecraft.getInstance().player;
		ItemStack stack = player.getMainHandItem();

		if (!(stack.getItem() instanceof ItemSniffer))
			stack = player.getOffhandItem();

		if (stack.getItem() instanceof ItemSniffer && stack.hasTag())
		{
			int[] coords = stack.getOrCreateTag().getIntArray("highlight");

			if (coords.length > 0)
			{
				MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
				Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.LINES);
				PoseStack poseStack = event.getMatrixStack();
				poseStack.pushPose();
				poseStack.translate(-cam.x(), -cam.y(),  -cam.z());

				float red = (float) WRConfig.highlightColorRed / 256;
				float green = (float) WRConfig.highlightColorGreen / 256;
				float blue = (float) WRConfig.highlightColorBlue / 256;

				for (int pos = 0; pos < coords.length; pos += 3)
				{
					int x = coords[pos];
					int y = coords[pos + 1];
					int z = coords[pos + 2];
					player.shouldRender(x, y, z);
					LevelRenderer.renderLineBox(poseStack, vertexConsumer, x, y, z, x + 1, y + 1, z + 1, red, green, blue, 1f);
				}

				poseStack.popPose();
				RenderSystem.disableDepthTest();
				RenderSystem.depthMask(false);
				bufferSource.endBatch(RenderType.LINES);
				RenderSystem.enableDepthTest();
				RenderSystem.depthMask(true);
			}
		}
	}
}
