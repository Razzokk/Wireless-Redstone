package rzk.wirelessredstone.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkEvent;
import rzk.wirelessredstone.item.SnifferItem;
import rzk.wirelessredstone.misc.WRConfig;

import java.util.function.Supplier;

public class SnifferHighlightRenderer
{
	public static void handleSnifferHighlightPacket(Supplier<NetworkEvent.Context> ctx, long timestamp, InteractionHand hand, BlockPos[] coords)
	{
		Player player = Minecraft.getInstance().player;
		ItemStack stack = player.getItemInHand(hand);
		if (stack.getItem() instanceof SnifferItem item)
			item.setHighlightedBlocks(timestamp, stack, coords);
	}

	@SubscribeEvent
	public static void renderSnifferHighlights(RenderLevelStageEvent event)
	{
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

		Player player = Minecraft.getInstance().player;
		ItemStack stack = player.getMainHandItem();

		if (!(stack.getItem() instanceof SnifferItem))
			stack = player.getOffhandItem();

		if (!(stack.getItem() instanceof SnifferItem) || !stack.hasTag()) return;

		ListTag coords = stack.getOrCreateTag().getList("highlights", Tag.TAG_COMPOUND);
		if (coords.isEmpty()) return;

		Vec3 cam = event.getCamera().getPosition();
		PoseStack poseStack = event.getPoseStack();
		poseStack.pushPose();
		poseStack.translate(-cam.x, -cam.y, -cam.z);

		float red = ((WRConfig.highlightColor >> 16) & 0xFF) / 256.0f;
		float green = ((WRConfig.highlightColor >> 8) & 0xFF) / 256.0f;
		float blue = (WRConfig.highlightColor & 0xFF) / 256.0f;

		RenderSystem.assertOnRenderThread();
		GlStateManager._depthMask(false);
		GlStateManager._disableCull();
		RenderSystem.disableDepthTest();
		RenderSystem.lineWidth(3f);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		builder.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);

		for (Tag posNbt : coords)
		{
			BlockPos pos = NbtUtils.readBlockPos((CompoundTag) posNbt);
			player.shouldRender(pos.getX(), pos.getY(), pos.getZ());
			LevelRenderer.renderLineBox(poseStack, builder, pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, red, green, blue, 1f);
		}

		tesselator.end();

		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.lineWidth(1f);

		poseStack.popPose();
	}
}
