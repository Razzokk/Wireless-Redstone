package rzk.wirelessredstone.ether;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import rzk.wirelessredstone.misc.Utils;
import rzk.wirelessredstone.registries.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class RedstoneChannel
{
	private final int frequency;
	// Contains only active transmitters
	private List<BlockPos> transmitters = new ArrayList<>();
	private List<BlockPos> receivers = new ArrayList<>();

	public RedstoneChannel(int frequency)
	{
		this.frequency = frequency;
	}
	
	public RedstoneChannel(CompoundTag tag)
	{
		frequency = Utils.readFrequency(tag);

		ListTag transmitterTags = tag.getList("transmitters", Tag.TAG_COMPOUND);
		for (Tag transmitterTag : transmitterTags)
			transmitters.add(NbtUtils.readBlockPos((CompoundTag) transmitterTag));

		ListTag receiverTags = tag.getList("receivers", Tag.TAG_COMPOUND);
		for (Tag receiverTag : receiverTags)
			receivers.add(NbtUtils.readBlockPos((CompoundTag) receiverTag));
	}

	public CompoundTag save()
	{
		CompoundTag tag = new CompoundTag();
		Utils.writeFrequency(tag, frequency);
		
		ListTag transmitterTags = new ListTag();
		for (BlockPos pos : transmitters)
			transmitterTags.add(NbtUtils.writeBlockPos(pos));
		tag.put("transmitters", transmitterTags);

		ListTag receiverTags = new ListTag();
		for (BlockPos pos : receivers)
			receiverTags.add(NbtUtils.writeBlockPos(pos));
		tag.put("receivers", receiverTags);
		
		return tag;
	}
	
	public void addTransmitter(Level level, BlockPos pos)
	{
		boolean empty = isInactive();
		transmitters.add(pos);
		if (empty) updateReceivers(level);
	}

	public void removeTransmitter(Level level, BlockPos pos)
	{
		transmitters.remove(pos);
		if (isInactive())
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

	public void updateReceiver(Level level, BlockPos pos)
	{
		if (level.isLoaded(pos))
			level.scheduleTick(pos, ModBlocks.REDSTONE_RECEIVER.get(), 1);
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

	public boolean isActive()
	{
		return !transmitters.isEmpty();
	}

	public boolean isInactive()
	{
		return transmitters.isEmpty();
	}
}
