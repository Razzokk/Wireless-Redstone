package rzk.wirelessredstone.ether;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import rzk.wirelessredstone.registries.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class Channel
{
	private final int freq;
	private List<BlockPos> transmitters;
	private List<BlockPos> receivers;

	public Channel(int freq)
	{
		this.freq = freq;
		transmitters = new ArrayList<>();
		receivers = new ArrayList<>();
	}

	public void addTransmitter(Level level, BlockPos pos)
	{
		boolean state = !transmitters.isEmpty();
		transmitters.add(pos);
		if (!state) updateReceivers(level);
	}

	public void removeTransmitter(Level level, BlockPos pos)
	{
		transmitters.remove(pos);
		if (transmitters.isEmpty()) updateReceivers(level);
	}

	public void addReceiver(Level level, BlockPos pos)
	{
		receivers.add(pos);
		updateReceiver(level, pos);
	}

	public void removeReceiver(Level level, BlockPos pos)
	{
		receivers.remove(pos);
	}

	public void updateReceiver(Level level, BlockPos pos)
	{
		if (level.isLoaded(pos))
			level.scheduleTick(pos, ModBlocks.RECEIVER.get(), 1);
	}

	public void updateReceivers(Level level)
	{
		for (BlockPos receiver : receivers)
			updateReceiver(level, receiver);
	}

	public boolean isActive()
	{
		return !transmitters.isEmpty();
	}
}
