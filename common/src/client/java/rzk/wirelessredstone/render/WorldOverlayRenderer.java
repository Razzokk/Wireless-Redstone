package rzk.wirelessredstone.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import rzk.wirelessredstone.item.LinkerItem;
import rzk.wirelessredstone.item.SnifferItem;
import rzk.wirelessredstone.misc.WRConfig;

public class WorldOverlayRenderer
{
	public static void render(World world, Vec3d cameraPosition, MatrixStack matrixStack, float tickDelta)
	{
		var player = MinecraftClient.getInstance().player;
		var stack = player.getMainHandStack();
		renderSnifferHighlights(world, player, stack, cameraPosition, matrixStack);
		renderLinkerTarget(world, player, stack, cameraPosition, matrixStack, tickDelta);
	}

	private static Tessellator renderLinesPre(Vec3d cameraPosition, MatrixStack matrixStack)
	{
		matrixStack.push();
		matrixStack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);

		RenderSystem.assertOnRenderThread();
		GlStateManager._depthMask(false);
		GlStateManager._disableCull();
		RenderSystem.disableDepthTest();
		RenderSystem.lineWidth(3f);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getRenderTypeLinesProgram);

		return RenderSystem.renderThreadTesselator();
	}

	private static void renderLinesPost(Tessellator tessellator, MatrixStack matrixStack)
	{
		tessellator.draw();

		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.lineWidth(1f);
		GlStateManager._enableCull();
		GlStateManager._depthMask(true);

		matrixStack.pop();
	}

	private static void renderSnifferHighlights(World world, PlayerEntity player, ItemStack stack, Vec3d cameraPosition, MatrixStack matrixStack)
	{
		var coords = SnifferItem.getHighlightedBlocks(stack);
		if (coords == null) coords = SnifferItem.getHighlightedBlocks(player.getOffHandStack());
		if (coords == null) return;

		var red = ((WRConfig.highlightColor >> 16) & 0xFF) / 256.0f;
		var green = ((WRConfig.highlightColor >> 8) & 0xFF) / 256.0f;
		var blue = (WRConfig.highlightColor & 0xFF) / 256.0f;

		var tessellator = renderLinesPre(cameraPosition, matrixStack);
		var builder = tessellator.getBuffer();
		builder.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);

		var matrix = matrixStack.peek();

		for (var pos : coords)
		{
			if (!player.shouldRender(pos.getX(), pos.getY(), pos.getZ())) continue;

			var state = world.getBlockState(pos);
			var shape = state.getOutlineShape(world, pos);
			RenderUtils.drawOutlineShape(builder, matrix, shape, pos, red, green, blue, 1);
		}

		renderLinesPost(tessellator, matrixStack);
	}

	private static void renderLinkerTarget(World world, PlayerEntity player, ItemStack stack, Vec3d cameraPosition, MatrixStack matrixStack, float tickDelta)
	{
		BlockPos target = LinkerItem.getTarget(stack);
		if (target == null) LinkerItem.getTarget(player.getOffHandStack());
		if (target == null || !player.shouldRender(target.getX(), target.getY(), target.getZ())) return;

		var red = ((WRConfig.linkerTargetColor >> 16) & 0xFF) / 256.0f;
		var green = ((WRConfig.linkerTargetColor >> 8) & 0xFF) / 256.0f;
		var blue = (WRConfig.linkerTargetColor & 0xFF) / 256.0f;

		var tessellator = renderLinesPre(cameraPosition, matrixStack);
		var builder = tessellator.getBuffer();
		builder.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);

		var time = ((world.getTime() + tickDelta) % 40) / 20;
		float alpha;

		if (time <= 1)
		{
			alpha = (3 - 2 * time) * time * time;
		}
		else
		{
			time -= 1;
			alpha = 1 - (3 - 2 * time) * time * time;
		}

		var state = world.getBlockState(target);
		var shape = state.getOutlineShape(world, target);
		var matrix = matrixStack.peek();
		RenderUtils.drawOutlineShape(builder, matrix, shape, target, red, green, blue, alpha);

		renderLinesPost(tessellator, matrixStack);
	}
}
