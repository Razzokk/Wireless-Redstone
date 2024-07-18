package rzk.wirelessredstone.misc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.ServerTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.api.ChunkLoadListener;
import rzk.wirelessredstone.item.RemoteItem;
import rzk.wirelessredstone.registry.ModItems;

public class WREvents
{
	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		PlayerEntity player = event.getEntity();
		World world = player.getWorld();
		ItemStack stack = player.getActiveItem();
		if (world.isClient || !stack.isOf(ModItems.remote)) return;
		((RemoteItem) stack.getItem()).onDeactivation(stack, world, player);
	}

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event)
	{
		if (!(event.getLevel() instanceof ServerWorld world)) return;
		if (!(event.getChunk() instanceof WorldChunk chunk)) return;

		var server = world.getServer();

		server.send(new ServerTask(1, () ->
		{
			var blockEntities = chunk.getBlockEntities().values();

			for (var blockEntity : blockEntities)
				if (blockEntity instanceof ChunkLoadListener be)
					be.onChunkLoad(world);
		}));
	}

	@SubscribeEvent
	public static void onChunkUnload(ChunkEvent.Unload event)
	{
		if (!(event.getLevel() instanceof ServerWorld world)) return;
		if (!(event.getChunk() instanceof WorldChunk chunk)) return;

		var blockEntities = chunk.getBlockEntities().values();

		for (var blockEntity : blockEntities)
			if (blockEntity instanceof ChunkLoadListener be)
				be.onChunkUnload(world);
	}

	public static <T> T register(RegisterEvent.RegisterHelper<T> helper, String name, T object)
	{
		helper.register(WirelessRedstone.identifier(name), object);
		return object;
	}
}
