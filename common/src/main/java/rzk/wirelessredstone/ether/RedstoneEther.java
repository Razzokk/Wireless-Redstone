package rzk.wirelessredstone.ether;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import rzk.wirelessredstone.misc.WRUtils;

import java.util.Collections;
import java.util.Set;

public class RedstoneEther extends PersistentState
{
	private static final String DATA_NAME = "redstone_ether";
	private final Int2ObjectMap<RedstoneChannel> channels = new Int2ObjectOpenHashMap<>();

	private static final PersistentState.Type<RedstoneEther> TYPE =
		new Type<>(RedstoneEther::new, RedstoneEther::new, null);

	private RedstoneEther() {}

	private RedstoneEther(NbtCompound nbt)
	{
		NbtList channelTags = nbt.getList("channels", NbtElement.COMPOUND_TYPE);

		for (NbtElement channelNbt : channelTags)
		{
			RedstoneChannel channel = new RedstoneChannel((NbtCompound) channelNbt);
			channels.put(channel.getFrequency(), channel);
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt)
	{
		NbtList channelNbts = new NbtList();
		for (RedstoneChannel channel : channels.values())
			channelNbts.add(channel.save());
		nbt.put("channels", channelNbts);

		return nbt;
	}

	public static RedstoneEther get(ServerWorld world)
	{
		return world.getPersistentStateManager().get(TYPE, DATA_NAME);
	}

	public static RedstoneEther getOrCreate(ServerWorld world)
	{
		return world.getPersistentStateManager().getOrCreate(TYPE, DATA_NAME);
	}

	private RedstoneChannel getChannel(int frequency)
	{
		return channels.get(frequency);
	}

	private RedstoneChannel getOrCreateChannel(int frequency)
	{
		RedstoneChannel channel = channels.get(frequency);

		if (channel == null)
		{
			channel = new RedstoneChannel(frequency);
			channels.put(frequency, channel);
		}

		return channel;
	}

	public void addTransmitter(World world, BlockPos pos, int frequency)
	{
		if (!WRUtils.isValidFrequency(frequency)) return;
		getOrCreateChannel(frequency).addTransmitter(world, pos);
		markDirty();
	}

	public void addRemote(World world, LivingEntity owner, int frequency)
	{
		if (!WRUtils.isValidFrequency(frequency)) return;
		getOrCreateChannel(frequency).addRemote(world, owner);
	}

	public void addReceiver(World world, BlockPos pos, int frequency)
	{
		if (!WRUtils.isValidFrequency(frequency)) return;
		getOrCreateChannel(frequency).addReceiver(world, pos);
	}

	public void removeTransmitter(World world, BlockPos pos, int frequency)
	{
		RedstoneChannel channel = getChannel(frequency);
		if (channel == null) return;

		channel.removeTransmitter(world, pos);
		if (channel.isEmpty()) channels.remove(frequency);
		markDirty();
	}

	public void removeRemote(World world, LivingEntity owner, int frequency)
	{
		RedstoneChannel channel = getChannel(frequency);
		if (channel == null) return;

		channel.removeRemote(world, owner);
		if (channel.isEmpty())
		{
			channels.remove(frequency);
			markDirty();
		}
	}

	public void removeReceiver(BlockPos pos, int frequency)
	{
		RedstoneChannel channel = getChannel(frequency);
		if (channel == null) return;

		channel.removeReceiver(pos);
		if (channel.isEmpty())
		{
			channels.remove(frequency);
			markDirty();
		}
	}

	public Set<BlockPos> getTransmitters(int frequency)
	{
		RedstoneChannel channel = getChannel(frequency);
		return channel != null ? channel.getTransmitters() : Collections.emptySet();
	}

	public boolean isFrequencyActive(int frequency)
	{
		RedstoneChannel channel = getChannel(frequency);
		return channel != null && channel.isActive();
	}
}
