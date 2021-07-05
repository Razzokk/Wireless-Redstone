package rzk.wirelessredstone.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModTiles;
import rzk.wirelessredstone.rsnetwork.Device;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;

import javax.annotation.Nullable;

public class TileFrequency extends TileEntity implements Device.Block
{
	private Type type;
	private short frequency;

	public TileFrequency(Type type)
	{
		super(ModTiles.frequency);
		this.type = type;
		frequency = 0;
	}

	public TileFrequency()
	{
		this(Type.TRANSMITTER);
	}

	@Override
	public short getFrequency()
	{
		return frequency;
	}

	public void setFrequency(short frequency)
	{
		if (this.frequency != frequency && !level.isClientSide)
		{
			RedstoneNetwork network = RedstoneNetwork.get((ServerWorld) level);
			BlockState state = level.getBlockState(worldPosition);

			if (!(isTransmitter() && state.is(ModBlocks.redstoneTransmitter) && !state.getValue(BlockStateProperties.POWERED)))
				network.changeDeviceFrequency(this, frequency);

			this.frequency = frequency;
			level.sendBlockUpdated(worldPosition, state, state, Constants.BlockFlags.BLOCK_UPDATE);
			setChanged();
		}
	}

	@Override
	public Type getDeviceType()
	{
		return type;
	}

	@Override
	public BlockPos getFreqPos()
	{
		return worldPosition;
	}

	@Override
	public CompoundNBT getUpdateTag()
	{
		return save(new CompoundNBT());
	}

	@Nullable
	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket(worldPosition, 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
	{
		load(level.getBlockState(pkt.getPos()), pkt.getTag());
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt)
	{
		super.load(state, nbt);
		type = nbt.getBoolean("transmitter") ? Type.TRANSMITTER : Type.RECEIVER;
		frequency = nbt.getShort("frequency");

		// Backwards compatibility
		if (nbt.contains("isTransmitter"))
			type = nbt.getBoolean("isTransmitter") ? Type.TRANSMITTER : Type.RECEIVER;
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt)
	{
		super.save(nbt);
		nbt.putBoolean("transmitter", isTransmitter());
		nbt.putShort("frequency", frequency);
		return nbt;
	}

	@Override
	public void onLoad()
	{
		super.onLoad();

		// For backwards compatibility
		if (WirelessRedstone.hasMissingBlockMappings && !level.isClientSide)
		{
			level.getServer().tell(new TickDelayedTask(1, () ->
			{
				if (level.isLoaded(worldPosition) && isReceiver() || (isTransmitter() && getBlockState().hasProperty(BlockStateProperties.POWERED) && getBlockState().getValue(BlockStateProperties.POWERED)))
				{
					RedstoneNetwork network = RedstoneNetwork.get((ServerWorld) level);
					network.addDevice(this);
				}
			}));
		}
		// End backwards compatibility

		if (!level.isClientSide && isReceiver())
		{
			level.getServer().tell(new TickDelayedTask(2, () ->
			{
				if (level.isLoaded(worldPosition))
				{
					RedstoneNetwork network = RedstoneNetwork.get((ServerWorld) level);
					BlockFrequency.setPoweredState(level, worldPosition, network.isChannelActive(frequency));
				}
			}));
		}
	}

	@Override
	public void setRemoved()
	{
		if (!level.isClientSide)
			RedstoneNetwork.get((ServerWorld) level).removeDevice(this);

		super.setRemoved();
	}
}
