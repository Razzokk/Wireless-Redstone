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
import rzk.wirelessredstone.WirelessRedstone;
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

				float red = (WirelessRedstone.highlightColor & (0xff << 16)) >> 16;
				float green = (WirelessRedstone.highlightColor & (0xff << 8)) >> 8;
				float blue = WirelessRedstone.highlightColor & 0xff;

				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();
				double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) event.getPartialTicks();
				double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) event.getPartialTicks();
				double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) event.getPartialTicks();
				buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
				buffer.setTranslation(-d0, -d1, -d2);

				for (int pos = 0; pos < coords.length; pos += 3)
				{
					int x = coords[pos];
					int y = coords[pos + 1];
					int z = coords[pos + 2];
					player.isInRangeToRender3d(x, y, z);
					RenderGlobal.drawBoundingBox(buffer, x, y, z, x + 1, y + 1, z + 1, red / 256, green / 256, blue / 256, 1f);
				}

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
