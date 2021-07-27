package rzk.wirelessredstone.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.Constants;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModTiles;
import rzk.wirelessredstone.rsnetwork.Device;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;

import javax.annotation.Nullable;

public abstract class TileFrequency extends BlockEntity implements Device.Block
{
	protected short frequency;

	private TileFrequency(BlockEntityType<? extends TileFrequency> entityType, BlockPos pos, BlockState state)
	{
		super(entityType, pos, state);
		frequency = 0;
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
			RedstoneNetwork network = RedstoneNetwork.get((ServerLevel) level);
			BlockState state = level.getBlockState(worldPosition);

			if (!(isTransmitter() && state.is(ModBlocks.redstoneTransmitter) && !state.getValue(BlockStateProperties.POWERED)))
				network.changeDeviceFrequency(this, frequency);

			this.frequency = frequency;
			level.sendBlockUpdated(worldPosition, state, state, Constants.BlockFlags.BLOCK_UPDATE);
			setChanged();
		}
	}

	@Override
	public BlockPos getFreqPos()
	{
		return worldPosition;
	}

	@Override
	public CompoundTag getUpdateTag()
	{
		return save(new CompoundTag());
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt)
	{
		load(pkt.getTag());
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket()
	{
		return new ClientboundBlockEntityDataPacket(worldPosition, 0, getUpdateTag());
	}

	@Override
	public void load(CompoundTag nbt)
	{
		super.load(nbt);
		frequency = nbt.getShort("frequency");
	}

	@Override
	public CompoundTag save(CompoundTag nbt)
	{
		super.save(nbt);
		nbt.putShort("frequency", frequency);
		return nbt;
	}

	@Override
	public void setRemoved()
	{
		if (!level.isClientSide)
			RedstoneNetwork.get((ServerLevel) level).removeDevice(this);

		super.setRemoved();
	}

	public static class Transmitter extends TileFrequency
	{
		public Transmitter(BlockPos pos, BlockState state)
		{
			super(ModTiles.transmitter, pos, state);
		}

		@Override
		public Type getDeviceType()
		{
			return Type.TRANSMITTER;
		}
	}

	public static class Receiver extends TileFrequency
	{
		public Receiver(BlockPos pos, BlockState state)
		{
			super(ModTiles.receiver, pos, state);
		}

		@Override
		public Type getDeviceType()
		{
			return Type.RECEIVER;
		}

		@Override
		public void onLoad()
		{
			super.onLoad();

			if (!level.isClientSide)
			{
				level.getServer().tell(new TickTask(2, () ->
				{
					if (level.isLoaded(worldPosition))
					{
						RedstoneNetwork network = RedstoneNetwork.get((ServerLevel) level);
						BlockFrequency.setPoweredState(level, worldPosition, network.isChannelActive(frequency));
					}
				}));
			}
		}
	}
}
