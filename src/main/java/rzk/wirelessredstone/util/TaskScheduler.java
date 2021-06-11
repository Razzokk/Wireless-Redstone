package rzk.wirelessredstone.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;

public class TaskScheduler
{
	private static final Object2ObjectMap<World, List<ScheduledTask>> SCHEDULED_WORLD_TASKS = new Object2ObjectOpenHashMap<>();

	public static void scheduleTask(ScheduledTask scheduledTask)
	{
		if (scheduledTask != null && scheduledTask.task != null && scheduledTask.delay >= 0)
		{
			World world = scheduledTask.world;

			if (!SCHEDULED_WORLD_TASKS.containsKey(world))
				SCHEDULED_WORLD_TASKS.put(world, new ObjectArrayList<>());

			SCHEDULED_WORLD_TASKS.get(world).add(scheduledTask);
		}
	}

	public static void scheduleTask(World world, int delay, Runnable task)
	{
		scheduleTask(new ScheduledTask(world, delay, task));
	}

	public static void scheduleTask(World world, Runnable task)
	{
		scheduleTask(world, 0, task);
	}

	public static void onWorldTick(World world)
	{
		if (SCHEDULED_WORLD_TASKS.containsKey(world) && SCHEDULED_WORLD_TASKS.get(world).size() > 0)
		{
			for (ScheduledTask scheduledTask : SCHEDULED_WORLD_TASKS.get(world))
				if (world.getTotalWorldTime() >= scheduledTask.scheduledTime + scheduledTask.delay)
					if (world.isRemote)
						Minecraft.getMinecraft().addScheduledTask(scheduledTask.task);
					else
						((WorldServer) world).addScheduledTask(scheduledTask.task);

			SCHEDULED_WORLD_TASKS.get(world).removeIf(scheduledTask -> scheduledTask.world.getTotalWorldTime() >= scheduledTask.scheduledTime + scheduledTask.delay);
		}
	}

	public static void onWorldUnload(World world)
	{
		SCHEDULED_WORLD_TASKS.remove(world);
	}

	public static void onServerStop()
	{
		SCHEDULED_WORLD_TASKS.clear();
	}

	public static class ScheduledTask
	{
		private final World world;
		private final int delay;
		private final Runnable task;
		private final long scheduledTime;

		public ScheduledTask(World world, int delay, Runnable task)
		{
			this.world = world;
			this.delay = delay;
			this.task = task;
			this.scheduledTime = world.getTotalWorldTime();
		}
	}
}
