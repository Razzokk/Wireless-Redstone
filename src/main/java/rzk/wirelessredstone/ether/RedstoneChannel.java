package rzk.wirelessredstone.ether;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.wirelessredstone.block.ModBlocks;
import rzk.wirelessredstone.misc.WRUtils;

import java.util.HashSet;
import java.util.Set;

public class RedstoneChannel
{
	private final int frequency;
	// Contains only active transmitters
	private final Set<BlockPos> transmitters = new HashSet<>();
	// Contains only currently listening/loaded receivers
	private final Set<BlockPos> receivers = new HashSet<>();

	public RedstoneChannel(int frequency)
	{
		this.frequency = frequency;
	}

	public RedstoneChannel(NbtCompound nbt)
	{
		frequency = WRUtils.readFrequency(nbt);

		NbtList transmitterNbts = nbt.getList("transmitters", NbtElement.COMPOUND_TYPE);
		for (NbtElement transmitterNbt : transmitterNbts)
			transmitters.add(NbtHelper.toBlockPos((NbtCompound) transmitterNbt));
	}

	public NbtCompound save()
	{
		NbtCompound nbt = new NbtCompound();
		WRUtils.writeFrequency(nbt, frequency);

		NbtList transmitterNbts = new NbtList();
		for (BlockPos pos : transmitters)
			transmitterNbts.add(NbtHelper.fromBlockPos(pos));
		nbt.put("transmitters", transmitterNbts);

		return nbt;
	}

	public void addTransmitter(World world, BlockPos pos)
	{
		boolean empty = isInactive();
		transmitters.add(pos);
		if (empty) updateReceivers(world);
	}

	public void removeTransmitter(World world, BlockPos pos)
	{
		transmitters.remove(pos);
		if (isInactive())
			updateReceivers(world);
	}

	public void addReceiver(World world, BlockPos pos)
	{
		receivers.add(pos);
		updateReceiver(world, pos);
	}

	public void removeReceiver(BlockPos pos)
	{
		receivers.remove(pos);
	}

	public void updateReceiver(World world, BlockPos pos)
	{
		world.scheduleBlockTick(pos, ModBlocks.REDSTONE_RECEIVER, 2);
	}

	public void updateReceivers(World world)
	{
		for (BlockPos receiver : receivers)
			updateReceiver(world, receiver);
	}

	public int getFrequency()
	{
		return frequency;
	}

	public Set<BlockPos> getTransmitters()
	{
		return transmitters;
	}

	public boolean isActive()
	{
		return !transmitters.isEmpty();
	}

	public boolean isInactive()
	{
		return transmitters.isEmpty();
	}

	public boolean isEmpty()
	{
		return transmitters.isEmpty() && receivers.isEmpty();
	}
}
