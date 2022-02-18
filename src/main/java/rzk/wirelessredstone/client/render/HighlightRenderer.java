package rzk.wirelessredstone.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.ItemSniffer;
import rzk.wirelessredstone.util.WRConfig;

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
				MatrixStack matrixStack = event.getMatrixStack();
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder builder = tessellator.getBuilder();
				Vector3d cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

				matrixStack.pushPose();
				matrixStack.translate(-cam.x(), -cam.y(), -cam.z());
				RenderSystem.disableTexture();
				RenderSystem.disableDepthTest();
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				RenderSystem.lineWidth(3);
				builder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

				float red = WRConfig.highlightColorRed / 255f;
				float green = WRConfig.highlightColorGreen / 255f;
				float blue = WRConfig.highlightColorBlue / 255f;

				for (int i = 0; i < coords.length; i += 3)
				{
					int x = coords[i];
					int y = coords[i + 1];
					int z = coords[i + 2];

					if (player.shouldRender(x, y, z))
						WorldRenderer.renderLineBox(matrixStack, builder, x, y, z, x + 1, y + 1, z + 1, red, green, blue, 1f);
				}

				tessellator.end();
				matrixStack.popPose();
				RenderSystem.enableTexture();
				RenderSystem.enableDepthTest();
				RenderSystem.disableBlend();
				RenderSystem.lineWidth(1);
			}
		}
	}
}
