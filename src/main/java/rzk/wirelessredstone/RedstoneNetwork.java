package rzk.wirelessredstone;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.shorts.Short2ObjectArrayMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.registry.ModBlocks;

public class RedstoneNetwork extends WorldSavedData
{
    public static final String DATA_NAME = "redstoneNetwork";

    private World world;
    private final Short2ObjectMap<ObjectSet<BlockPos>> transmitters = new Short2ObjectArrayMap<>();
    private final Short2ObjectMap<ObjectSet<BlockPos>> receivers = new Short2ObjectArrayMap<>();
    private final Short2ObjectMap<String> frequencyNames = new Short2ObjectArrayMap<>();

    public RedstoneNetwork(String name)
    {
        super(name);
    }

    public RedstoneNetwork()
    {
        this(DATA_NAME);
    }

    public void updateReceivers(short frequency, boolean powered)
    {
        if (receivers.containsKey(frequency))
            for (BlockPos pos : receivers.get(frequency))
                if (world.isBlockLoaded(pos))
                    ((BlockFrequency) ModBlocks.receiver).updateReceiver(world, pos, powered);
    }

    public void addTransmitter(BlockPos pos, short frequency)
    {
        transmitters.putIfAbsent(frequency, new ObjectArraySet<>());
        transmitters.get(frequency).add(pos);
        frequencyNames.putIfAbsent(frequency, null);
        updateReceivers(frequency, true);
    }

    public void addReceiver(BlockPos pos, short frequency)
    {
        receivers.putIfAbsent(frequency, new ObjectArraySet<>());
        receivers.get(frequency).add(pos);
        frequencyNames.putIfAbsent(frequency, null);

        if (transmitters.containsKey(frequency) && !transmitters.get(frequency).isEmpty())
            ((BlockFrequency) ModBlocks.receiver).updateReceiver(world, pos, true);
    }

    public void checkAndRemoveFrequencyNames(short frequency)
    {
        if (!transmitters.containsKey(frequency) && !receivers.containsKey(frequency))
            frequencyNames.remove(frequency);
    }

    public void removeTransmitter(BlockPos pos, short frequency)
    {
        if (transmitters.containsKey(frequency))
        {
            transmitters.get(frequency).remove(pos);

            if (transmitters.get(frequency).isEmpty())
            {
                transmitters.remove(frequency);
                updateReceivers(frequency, false);
            }
        }
    }

    public void removeReceiver(BlockPos pos, short frequency)
    {
        if (receivers.containsKey(frequency))
            receivers.get(frequency).remove(pos);
    }

    public void changeTransmitterFrequency(BlockPos pos, short oldFrequency, short newFrequency)
    {
        removeTransmitter(pos, oldFrequency);
        addTransmitter(pos, newFrequency);
    }

    public void changeReceiverFrequency(BlockPos pos, short oldFrequency, short newFrequency)
    {
        removeReceiver(pos, oldFrequency);
        addReceiver(pos, newFrequency);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        //placedBlocks = nbt.getInteger("placed");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        for (short frequency : frequencyNames.keySet())
        {
            NBTTagCompound freqNbt = new NBTTagCompound();
        }
        //nbt.setShort("placed", placedBlocks);
        return nbt;
    }

    public World getWorld()
    {
        return world;
    }

    public static RedstoneNetwork getOrCreate(World world)
    {
        if (world == null)
            return null;

        RedstoneNetwork instance = null;
        MapStorage mapStorage = world.getMapStorage();

        if (mapStorage != null)
        {
            instance = (RedstoneNetwork) mapStorage.getOrLoadData(RedstoneNetwork.class, DATA_NAME);

            if (instance == null)
            {
                instance = new RedstoneNetwork();
                mapStorage.setData(DATA_NAME, instance);
            }

            instance.world = world;
        }

        return instance;
    }
}
