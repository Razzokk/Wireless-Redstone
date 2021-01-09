package rzk.wirelessredstone;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class RedstoneNetwork extends WorldSavedData
{
    public static final String DATA_NAME = "redstoneNetwork";

    private World world;
    private int placedBlocks = 0;

    public RedstoneNetwork(String name)
    {
        super(name);
    }

    public RedstoneNetwork()
    {
        this(DATA_NAME);
    }

    public World getWorld()
    {
        return world;
    }

    public void placeBlock()
    {
        placedBlocks++;
        markDirty();
    }

    public int getPlacedBlocks()
    {
        return placedBlocks;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        placedBlocks = nbt.getInteger("placed");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("placed", placedBlocks);
        return nbt;
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
