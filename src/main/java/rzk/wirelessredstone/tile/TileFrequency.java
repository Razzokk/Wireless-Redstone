package rzk.wirelessredstone.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import rzk.wirelessredstone.RedstoneNetwork;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.util.FrequencyType;

public class TileFrequency extends TileEntity
{
    private FrequencyType type;
    private short frequency;

    public TileFrequency() {}

    public TileFrequency(FrequencyType type)
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

            if (isTransmitter() && world.getBlockState(pos).getValue(BlockFrequency.POWERED))
                network.changeReceiverFrequency(pos, this.frequency, frequency);
            else if (isReceiver())
                network.changeReceiverFrequency(pos, this.frequency, frequency);

            this.frequency = frequency;
            markDirty();
        }
    }

    private boolean isTransmitter()
    {
        return type == FrequencyType.TRANSMITTER;
    }

    private boolean isReceiver()
    {
        return type == FrequencyType.RECEIVER;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        frequency = nbt.getShort("frequency");
        type = nbt.getBoolean("type") ? FrequencyType.TRANSMITTER : FrequencyType.RECEIVER;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setShort("frequency", frequency);
        nbt.setBoolean("type", type == FrequencyType.TRANSMITTER);
        return nbt;
    }
}
