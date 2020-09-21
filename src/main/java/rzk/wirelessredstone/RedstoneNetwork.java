package rzk.wirelessredstone;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import rzk.lib.util.ObjectUtils;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.registry.ModBlocks;

public class RedstoneNetwork extends WorldSavedData
{
	public static final String NAME = "redstone_network";
	private final Int2IntMap activeTransmitters = new Int2IntArrayMap();
	private final Int2ObjectMap<LongSet> receivers = new Int2ObjectArrayMap<>();
	private World world;

	public RedstoneNetwork(String name)
	{
		super(name);
	}

	public RedstoneNetwork()
	{
		super(NAME);
	}

	public static RedstoneNetwork getOrCreate(World world)
	{
		return ObjectUtils.mapIfCastable(world, ServerWorld.class, serverWorld -> serverWorld.getSavedData().getOrCreate(RedstoneNetwork::new, NAME).setWorld(serverWorld));
	}

	private RedstoneNetwork setWorld(World world)
	{
		this.world = world;
		return this;
	}

	public void addActiveTransmitter(int frequency)
	{
		if (activeTransmitters.containsKey(frequency))
			activeTransmitters.replace(frequency, activeTransmitters.getOrDefault(frequency, 0) + 1);
		else
			activeTransmitters.put(frequency, 1);
		updateReceiversOnFrequency(frequency);
		markDirty();
	}

	public void removeActiveTransmitter(int frequency)
	{
		int currentTransmitters = activeTransmitters.getOrDefault(frequency, 0);
		activeTransmitters.replace(frequency, currentTransmitters > 0 ? currentTransmitters - 1 : 0);

		if (activeTransmitters.get(frequency) <= 0)
			activeTransmitters.remove(frequency);

		updateReceiversOnFrequency(frequency);
		markDirty();
	}

	public void changeActiveTransmitterFrequency(int oldFrequency, int newFrequency)
	{
		removeActiveTransmitter(oldFrequency);
		addActiveTransmitter(newFrequency);
	}

	public int getActiveTransmitters(int frequency)
	{
		return activeTransmitters.getOrDefault(frequency, 0);
	}

	public void addReceiver(int frequency, BlockPos pos, boolean updateReceiver)
	{
		if (receivers.containsKey(frequency))
		{
			receivers.getOrDefault(frequency, new LongArraySet()).add(pos.toLong());
		}
		else
		{
			LongSet rxs = new LongArraySet();
			rxs.add(pos.toLong());
			receivers.put(frequency, rxs);
		}

		markDirty();

		if (updateReceiver)
			updateReceiver(frequency, pos);
	}

	public void addReceiver(int frequency, BlockPos pos)
	{
		addReceiver(frequency, pos, true);
	}

	public void removeReceiver(int frequency, BlockPos pos)
	{
		if (receivers.containsKey(frequency))
		{
			receivers.get(frequency).remove(pos.toLong());
			if (receivers.get(frequency).isEmpty())
				receivers.remove(frequency);
		}

		markDirty();
	}

	public void updateReceiver(int frequency, BlockPos pos)
	{
		if (world.isAreaLoaded(pos, 0))
			ObjectUtils.ifCastable(ModBlocks.RECEIVER, BlockFrequency.class, block ->
					block.setPoweredState(world.getBlockState(pos), world, pos, getActiveTransmitters(frequency) > 0));
	}

	public void changeReceiverFrequency(int oldFrequency, int newFrequency, BlockPos pos)
	{
		removeReceiver(oldFrequency, pos);
		addReceiver(newFrequency, pos);
	}

	public void updateReceiversOnFrequency(int frequency)
	{
		if (receivers.containsKey(frequency))
			for (Long pos : receivers.get(frequency))
				updateReceiver(frequency, BlockPos.fromLong(pos));
	}

	@Override
	public void read(CompoundNBT compound)
	{
		int[] txFrequencies = compound.getIntArray("txFrequencies");
		for (int frequency : txFrequencies)
			activeTransmitters.put(frequency, compound.getInt("activeTransmitters_" + frequency));
	}

	@Override
	public CompoundNBT write(CompoundNBT compound)
	{
		int[] txFrequencies = activeTransmitters.keySet().toIntArray();
		compound.putIntArray("txFrequencies", txFrequencies);
		for (int frequency : txFrequencies)
			compound.putInt("activeTransmitters_" + frequency, activeTransmitters.get(frequency));

		return compound;
	}
}
