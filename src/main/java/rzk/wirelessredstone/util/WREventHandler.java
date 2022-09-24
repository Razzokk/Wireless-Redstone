package rzk.wirelessredstone.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import rzk.wirelessredstone.api.IChunkListener;
import rzk.wirelessredstone.item.ItemRemote;

import java.util.Map;

public class WREventHandler
{
	private static void powerOffRemote(World world, ItemStack stack)
	{
		if (!world.isRemote && stack.getItem() instanceof ItemRemote && ItemRemote.isPowered(stack))
			ItemRemote.setPowered(world, stack, false);
	}

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event)
	{
		Map<BlockPos, TileEntity> tiles = event.getChunk().getTileEntityMap();
		for (TileEntity tile : tiles.values())
			if (tile instanceof IChunkListener)
				((IChunkListener) tile).onChunkLoad();
	}

	@SubscribeEvent
	public static void onChunkUnload(ChunkEvent.Unload event)
	{
		Map<BlockPos, TileEntity> tiles = event.getChunk().getTileEntityMap();
		for (TileEntity tile : tiles.values())
			if (tile instanceof IChunkListener)
				((IChunkListener) tile).onChunkUnload();
	}

	@SubscribeEvent
	public static void onPlayerToss(ItemTossEvent event)
	{
		World world = event.getPlayer().getEntityWorld();
		powerOffRemote(world, event.getEntityItem().getItem());
	}

	@SubscribeEvent
	public static void onPlayerDrops(PlayerDropsEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.getEntityWorld();
		powerOffRemote(world, player.getActiveItemStack());
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		EntityPlayer player = event.player;
		World world = player.getEntityWorld();
		powerOffRemote(world, player.getActiveItemStack());
	}
}
