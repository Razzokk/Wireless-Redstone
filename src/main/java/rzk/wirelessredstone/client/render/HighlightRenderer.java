package rzk.wirelessredstone.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rzk.wirelessredstone.item.ItemSniffer;

@SideOnly(Side.CLIENT)
public class HighlightRenderer
{
	@SubscribeEvent
	public static void onRenderWorldLast(RenderWorldLastEvent event)
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		ItemStack stack = player.getHeldItemMainhand();

		if (!(stack.getItem() instanceof ItemSniffer))
			stack = player.getHeldItemOffhand();

		if (stack.getItem() instanceof ItemSniffer && stack.hasTagCompound())
		{
			int[] coords = stack.getTagCompound().getIntArray("highlight");

			if (coords.length > 0)
			{
				GlStateManager.glLineWidth(2.5f);
				GlStateManager.disableDepth();
				GlStateManager.disableLighting();
				GlStateManager.disableTexture2D();

				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) event.getPartialTicks();
				double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) event.getPartialTicks();
				double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) event.getPartialTicks();
				buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
				buffer.setTranslation(-d0, -d1, -d2);

				for (int pos = 0; pos < coords.length; pos += 3)
					RenderGlobal.drawBoundingBox(buffer, coords[pos], coords[pos + 1], coords[pos + 2], coords[pos] + 1, coords[pos + 1] + 1, coords[pos + 2] + 1, 1f, 0.25f, 0.25f, 1f);

				buffer.setTranslation(0, 0, 0);
				tessellator.draw();
				GlStateManager.enableTexture2D();
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
				GlStateManager.glLineWidth(1.0f);
			}
		}
	}
}
