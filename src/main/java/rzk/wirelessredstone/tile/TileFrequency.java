package rzk.wirelessredstone.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import rzk.wirelessredstone.util.FrequencyType;

public class TileFrequency extends TileEntity
{
    private FrequencyType type;
    private int frequency;

    public TileFrequency() {}

    public TileFrequency(FrequencyType type)
    {
        this.type = type;
    }

    public int getFrequency()
    {
        return frequency;
    }

    public void setFrequency(int frequency)
    {
        this.frequency = frequency;
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        frequency = nbt.getInteger("frequency");
        type = nbt.getBoolean("type") ? FrequencyType.TRANSMITTER : FrequencyType.RECEIVER;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("frequency", frequency);
        nbt.setBoolean("type", type == FrequencyType.TRANSMITTER);
        return nbt;
    }
}
