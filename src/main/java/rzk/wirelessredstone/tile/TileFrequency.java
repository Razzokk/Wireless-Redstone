package rzk.wirelessredstone.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.wirelessredstone.RedstoneNetwork;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.util.DeviceType;

import javax.annotation.Nullable;

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
            IBlockState state = world.getBlockState(pos);

            if (!(type == DeviceType.TRANSMITTER && !state.getValue(BlockFrequency.POWERED)))
                network.changeDeviceFrequency(this.frequency, frequency, pos, type);

            this.frequency = frequency;
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(pos, 3, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        handleUpdateTag(pkt.getNbtCompound());
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
