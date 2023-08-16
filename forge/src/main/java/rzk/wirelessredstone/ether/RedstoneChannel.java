package rzk.wirelessredstone.ether;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
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

	public RedstoneChannel(CompoundTag nbt)
	{
		frequency = WRUtils.readFrequency(nbt);

		ListTag transmitterNbts = nbt.getList("transmitters", CompoundTag.TAG_COMPOUND);
		for (Tag transmitterNbt : transmitterNbts)
			transmitters.add(NbtUtils.readBlockPos((CompoundTag) transmitterNbt));
	}

	public CompoundTag save()
	{
		CompoundTag nbt = new CompoundTag();
		WRUtils.writeFrequency(nbt, frequency);

		ListTag transmitterNbts = new ListTag();
		for (BlockPos pos : transmitters)
			transmitterNbts.add(NbtUtils.writeBlockPos(pos));
		nbt.put("transmitters", transmitterNbts);

		return nbt;
	}

	public void addTransmitter(Level level, BlockPos pos)
	{
		boolean empty = !isActive();
		transmitters.add(pos);
		if (empty) updateReceivers(level);
	}

	public void removeTransmitter(Level level, BlockPos pos)
	{
		transmitters.remove(pos);
		if (!isActive())
			updateReceivers(level);
	}

	public void addReceiver(Level level, BlockPos pos)
	{
		receivers.add(pos);
		updateReceiver(level, pos);
	}

	public void removeReceiver(BlockPos pos)
	{
		receivers.remove(pos);
	}

	public void addRemote(Level level, LivingEntity owner)
	{
		boolean empty = !isActive();
		remotes.add(owner);
		if (empty) updateReceivers(level);
	}

	public void removeRemote(Level level, LivingEntity owner)
	{
		remotes.remove(owner);
		if (!isActive())
			updateReceivers(level);
	}

	public void updateReceiver(Level level, BlockPos pos)
	{
		level.scheduleTick(pos, ModBlocks.REDSTONE_RECEIVER.get(), 2);
	}

	public void updateReceivers(Level level)
	{
		for (BlockPos receiver : receivers)
			updateReceiver(level, receiver);
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
