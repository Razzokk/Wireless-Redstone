package rzk.wirelessredstone;

import it.unimi.dsi.fastutil.shorts.Short2ObjectArrayMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.util.DeviceType;

public class RedstoneNetwork extends WorldSavedData
{
    public static final String DATA_NAME = "redstoneNetwork";

    private World world;
    private final Short2ObjectMap<Channel> basic;

    public RedstoneNetwork(String name)
    {
        super(name);
        basic = new Short2ObjectArrayMap<>();
    }

    public void updateReceivers(short frequency)
    {
        if (basic.containsKey(frequency))
        {
            Channel channel = basic.get(frequency);
            boolean isActive = channel.isActive();

            for (BlockPos receiver : channel.getReceivers())
                ((BlockFrequency) ModBlocks.redstoneReceiver).setPoweredState(world.getBlockState(receiver), world, receiver, isActive);
        }
    }

    public void addDevice(short frequency, BlockPos pos, DeviceType type)
    {
        basic.putIfAbsent(frequency, Channel.create(frequency, Channel.Type.BASIC));
        basic.get(frequency).addDevice(pos, type);

        if (type == DeviceType.TRANSMITTER)
            updateReceivers(frequency);

        markDirty();
    }

    public void removeDevice(short frequency, BlockPos pos, DeviceType type)
    {
        if (basic.containsKey(frequency))
        {
            basic.get(frequency).removeDevice(pos, type);

            if (type == DeviceType.TRANSMITTER)
                updateReceivers(frequency);

            markDirty();
        }
    }

    public void changeDeviceFrequency(short oldFrequency, short newFrequency, BlockPos pos, DeviceType type)
    {
        removeDevice(oldFrequency, pos, type);
        addDevice(newFrequency, pos, type);

        if (type == DeviceType.RECEIVER)
            ((BlockFrequency) ModBlocks.redstoneReceiver).setPoweredState(world.getBlockState(pos), world, pos, basic.get(newFrequency).isActive());
    }

    public boolean isChannelActive(short frequency)
    {
        return basic.containsKey(frequency) && basic.get(frequency).isActive();
    }

    public RedstoneNetwork()
    {
        this(DATA_NAME);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        if (nbt.hasKey("basic"))
        {
            NBTTagList basicNBT = nbt.getTagList("basic", 10);
            for (NBTBase channelNBT : basicNBT)
            {
                Channel channel = Channel.fromNBT((NBTTagCompound) channelNBT);

                if (channel != null && !channel.isEmpty())
                    basic.put(channel.getFrequency(), channel);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        if (!basic.isEmpty())
        {
            NBTTagList basicNBT = new NBTTagList();
            basic.short2ObjectEntrySet().removeIf(entry -> entry.getValue().isEmpty());
            basic.values().forEach(channel -> basicNBT.appendTag(channel.toNBT()));
            nbt.setTag("basic", basicNBT);
        }

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
