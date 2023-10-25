package rzk.wirelessredstone.ether;

import net.minecraft.entity.LivingEntity;
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
	// Player must be holding and activating remote, thus only one remote per player can be active
	// Additionally, remotes do not need to be saved because if the player logs off the remote gets deactivated,
	// same holds for shutting down the server/world.
	private final Set<LivingEntity> remotes = new HashSet<>();

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
		boolean empty = !isActive();
		transmitters.add(pos);
		if (empty) updateReceivers(world);
	}

	public void removeTransmitter(World world, BlockPos pos)
	{
		transmitters.remove(pos);
		if (!isActive())
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

	public void addRemote(World world, LivingEntity owner)
	{
		boolean empty = !isActive();
		remotes.add(owner);
		if (empty) updateReceivers(world);
	}

	public void removeRemote(World world, LivingEntity owner)
	{
		remotes.remove(owner);
		if (!isActive())
			updateReceivers(world);
	}

	public void updateReceiver(World world, BlockPos pos)
	{
		world.scheduleBlockTick(pos, ModBlocks.redstoneReceiver, 2);
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
		return !transmitters.isEmpty() || !remotes.isEmpty();
	}

	public boolean isEmpty()
	{
		return transmitters.isEmpty() && receivers.isEmpty() && remotes.isEmpty();
	}
}
