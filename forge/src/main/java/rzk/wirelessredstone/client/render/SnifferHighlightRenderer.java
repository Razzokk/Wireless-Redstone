package rzk.wirelessredstone.client.render;

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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.item.SnifferItem;
import rzk.wirelessredstone.misc.WRConfig;

public class SnifferHighlightRenderer
{
	public static void handleSnifferHighlightPacket(CustomPayloadEvent.Context ctx, long timestamp, Hand hand, BlockPos[] coords)
	{
		PlayerEntity player = MinecraftClient.getInstance().player;
		ItemStack stack = player.getStackInHand(hand);
		if (stack.getItem() instanceof SnifferItem item)
			item.setHighlightedBlocks(timestamp, stack, coords);
	}

	@SubscribeEvent
	public static void renderSnifferHighlights(RenderLevelStageEvent event)
	{
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

		PlayerEntity player = MinecraftClient.getInstance().player;
		ItemStack stack = player.getMainHandStack();

		if (!(stack.getItem() instanceof SnifferItem))
			stack = player.getOffHandStack();

		if (!(stack.getItem() instanceof SnifferItem) || !stack.hasNbt()) return;

		NbtList coords = stack.getOrCreateNbt().getList("highlights", NbtElement.COMPOUND_TYPE);
		if (coords.isEmpty()) return;

		Vec3d cam = event.getCamera().getPos();
		MatrixStack matrices = event.getPoseStack();
		matrices.push();
		matrices.translate(-cam.x, -cam.y, -cam.z);

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

		for (NbtElement posNbt : coords)
		{
			BlockPos pos = NbtHelper.toBlockPos((NbtCompound) posNbt);
			player.shouldRender(pos.getX(), pos.getY(), pos.getZ());
			WorldRenderer.drawBox(matrices, builder, pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, red, green, blue, 1f);
		}

		tessellator.draw();

		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.lineWidth(1f);

		matrices.pop();
	}
}
