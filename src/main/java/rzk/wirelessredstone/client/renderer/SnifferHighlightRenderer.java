package rzk.wirelessredstone.client.renderer;

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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.item.SnifferItem;
import rzk.wirelessredstone.misc.Config;

public class SnifferHighlightRenderer
{
	@SubscribeEvent
	public static void renderSnifferHighlights(RenderLevelLastEvent event)
	{
		Player player = Minecraft.getInstance().player;
		ItemStack stack = player.getMainHandItem();

		if (!(stack.getItem() instanceof SnifferItem))
			stack = player.getOffhandItem();

		if (!(stack.getItem() instanceof SnifferItem) || !stack.hasTag()) return;

		ListTag coords = stack.getOrCreateTag().getList("highlights", Tag.TAG_COMPOUND);
		if (coords.isEmpty()) return;

		Vec3 cam = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
		PoseStack poseStack = event.getPoseStack();
		poseStack.pushPose();
		poseStack.translate(-cam.x(), -cam.y(),  -cam.z());

		float red = Config.highlightColorRed / 256.0f;
		float green = Config.highlightColorGreen / 256.0f;
		float blue = Config.highlightColorBlue / 256.0f;

		Tesselator tesselator = Tesselator.getInstance();
		BufferBuilder builder = tesselator.getBuilder();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		builder.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

		RenderSystem.disableTexture();
		RenderSystem.disableDepthTest();
		RenderSystem.lineWidth(3); // Doesn't actually work because minecraft

		for (Tag posTag : coords)
		{
			BlockPos pos = NbtUtils.readBlockPos((CompoundTag) posTag);
			player.shouldRender(pos.getX(), pos.getY(), pos.getZ());
			LevelRenderer.renderLineBox(poseStack, builder, pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1, red, green, blue, 1f);
		}

		tesselator.end();
		RenderSystem.enableTexture();
		RenderSystem.enableDepthTest();
		poseStack.popPose();
	}
}
