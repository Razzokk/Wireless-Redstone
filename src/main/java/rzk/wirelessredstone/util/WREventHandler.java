package rzk.wirelessredstone.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rzk.wirelessredstone.item.ItemRemote;
import rzk.wirelessredstone.item.ItemSniffer;

public class WREventHandler
{
	private static void powerOffRemote(World world, ItemStack stack)
	{
		if (!world.isRemote && stack.getItem() instanceof ItemRemote && ItemRemote.isPowered(stack))
			ItemRemote.setPowered(world, stack, false);
	}

	private static void removeSnifferBlocks(World world, ItemStack stack)
	{
		if (!world.isRemote && stack.getItem() instanceof ItemSniffer)
			ItemSniffer.removeHighlightBlocks(stack);
	}

	private static void removeSnifferBlocks(World world, EntityPlayer player)
	{
		ItemStack stack = player.getHeldItemMainhand();

		if (!(stack.getItem() instanceof ItemSniffer))
			stack = player.getHeldItemOffhand();

		removeSnifferBlocks(world, stack);
	}

	@SubscribeEvent
	public static void onPlayerToss(ItemTossEvent event)
	{
		World world = event.getPlayer().getEntityWorld();
		ItemStack stack = event.getEntityItem().getItem();
		powerOffRemote(world, stack);
		removeSnifferBlocks(world, stack);
	}

	@SubscribeEvent
	public static void onPlayerDrops(PlayerDropsEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.getEntityWorld();
		powerOffRemote(world, player.getActiveItemStack());
		removeSnifferBlocks(world, player);
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();
		powerOffRemote(world, player.getActiveItemStack());
		removeSnifferBlocks(world, player);
	}

	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event)
	{
		TaskScheduler.onWorldTick(event.world);
	}

	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload event)
	{
		TaskScheduler.onWorldUnload(event.getWorld());
	}

	@SideOnly(Side.CLIENT)
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
