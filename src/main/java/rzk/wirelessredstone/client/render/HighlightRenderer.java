package rzk.wirelessredstone.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.client.ClientSubscriber;
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
				BufferBuilder builder = Tessellator.getInstance().getBuilder();
				Vector3d playerPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

				matrixStack.pushPose();
				matrixStack.translate(-playerPos.x(), -playerPos.y(), -playerPos.z());
				builder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

				RenderSystem.disableTexture();
				RenderSystem.disableDepthTest();
				RenderSystem.enableAlphaTest();
				RenderSystem.defaultAlphaFunc();
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				RenderSystem.lineWidth(3);

				int red = WRConfig.highlightColorRed;
				int green = WRConfig.highlightColorGreen;
				int blue = WRConfig.highlightColorBlue;

				for (int i = 0; i < coords.length; i += 3)
				{
					int x = coords[i];
					int y = coords[i + 1];
					int z = coords[i + 2];

					if (player.shouldRender(x, y, z))
						renderLineShape(matrixStack, builder, VoxelShapes.block(), x, y, z, red, green, blue, 255);
				}

				builder.end();
				WorldVertexBufferUploader.end(builder);
				matrixStack.popPose();
			}
		}
	}

	public static void renderLineShape(MatrixStack matrixStack, IVertexBuilder builder, VoxelShape shape, double posX, double posY, double posZ, int red, int green, int blue, int alpha)
	{
		Matrix4f matrix = matrixStack.last().pose();

		shape.forAllEdges((minX, minY, minZ, maxX, maxY, maxZ) ->
		{
			builder.vertex(matrix, (float) (minX + posX), (float) (minY + posY), (float) (minZ + posZ)).color(red, green, blue, alpha).endVertex();
			builder.vertex(matrix, (float) (maxX + posX), (float) (maxY + posY), (float) (maxZ + posZ)).color(red, green, blue, alpha).endVertex();
		});
	}
}
