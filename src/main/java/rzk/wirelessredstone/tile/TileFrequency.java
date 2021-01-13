package rzk.wirelessredstone.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import rzk.wirelessredstone.RedstoneNetwork;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.util.DeviceType;

public class TileFrequency extends TileEntity
{
    private DeviceType type;
    private short frequency;

    public TileFrequency() {}

    public TileFrequency(DeviceType type)
    {
        this.type = type;
        this.frequency = 0;
    }

    public short getFrequency()
    {
        return frequency;
    }

    public void setFrequency(short frequency)
    {
        if (this.frequency != frequency)
        {
            RedstoneNetwork network = RedstoneNetwork.getOrCreate(world);
            network.changeDeviceFrequency(this.frequency, frequency, pos, type);

            this.frequency = frequency;
            markDirty();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        frequency = nbt.getShort("frequency");
        type = nbt.getBoolean("type") ? DeviceType.TRANSMITTER : DeviceType.RECEIVER;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setShort("frequency", frequency);
        nbt.setBoolean("type", type == DeviceType.TRANSMITTER);
        return nbt;
    }
}
