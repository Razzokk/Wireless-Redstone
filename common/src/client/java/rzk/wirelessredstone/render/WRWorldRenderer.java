package rzk.wirelessredstone.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import rzk.wirelessredstone.item.SnifferItem;
import rzk.wirelessredstone.misc.WRConfig;

public class WRWorldRenderer
{
	public static void renderAfterTranslucent(World world, Vec3d cameraPosition, MatrixStack matrixStack, float tickDelta)
	{
		matrixStack.push();
		matrixStack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);

		PlayerEntity player = MinecraftClient.getInstance().player;
		ItemStack stack = player.getMainHandStack();
		renderSnifferHighlights(player, stack, matrixStack);

		matrixStack.pop();
	}

	private static void renderSnifferHighlights(PlayerEntity player, ItemStack stack, MatrixStack matrixStack)
	{
		var coords = SnifferItem.getHighlightedBlocks(stack);
		if (coords == null) coords = SnifferItem.getHighlightedBlocks(player.getOffHandStack());
		if (coords == null) return;

		float red = ((WRConfig.highlightColor >> 16) & 0xFF) / 256.0f;
		float green = ((WRConfig.highlightColor >> 8) & 0xFF) / 256.0f;
		float blue = (WRConfig.highlightColor & 0xFF) / 256.0f;

		RenderSystem.assertOnRenderThread();
		GlStateManager._depthMask(false);
		GlStateManager._disableCull();
		RenderSystem.disableDepthTest();
		RenderSystem.lineWidth(3f);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder builder = tessellator.getBuffer();
		builder.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);

		for (var pos : coords)
		{
			player.shouldRender(pos.getX(), pos.getY(), pos.getZ());
			WorldRenderer.drawBox(matrixStack, builder, pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, red, green, blue, 1f);
		}

		tessellator.draw();

		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.lineWidth(1f);
	}
}
