package rzk.wirelessredstone.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

public class WRWorldRendererFabric
{
	public static void renderAfterTranslucent(WorldRenderContext worldRenderContext)
	{
		var world = worldRenderContext.world();
		var cameraPosition = worldRenderContext.camera().getPos();
		var matrixStack = worldRenderContext.matrixStack();
		var tickDelta = worldRenderContext.tickDelta();
		WRWorldRenderer.renderAfterTranslucent(world, cameraPosition, matrixStack, tickDelta);
	}
}
