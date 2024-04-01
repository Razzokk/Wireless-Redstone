package rzk.wirelessredstone.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.api.SideConnectable;
import rzk.wirelessredstone.api.SidedBitSet;
import rzk.wirelessredstone.misc.WRUtils;

public abstract class RedstoneTransceiverBlockEntity extends BlockEntity implements SideConnectable
{
	protected int frequency = WRUtils.INVALID_FREQUENCY;
	protected SidedBitSet connections = SidedBitSet.allSet();

	public RedstoneTransceiverBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state)
	{
		super(blockEntityType, pos, state);
	}

	protected abstract void onFrequencyChange(int oldFrequency, int newFrequency);

	public int getFrequency()
	{
		return frequency;
	}

	public void setFrequency(int frequency)
	{
		if (frequency == this.frequency) return;
		onFrequencyChange(this.frequency, frequency);
		this.frequency = frequency;
		markDirty();
		var state = getCachedState();
		world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
	}

	@Nullable
	@Override
	public Packet<ClientPlayPacketListener> toUpdatePacket()
	{
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt()
	{
		NbtCompound nbt = new NbtCompound();
		WRUtils.writeFrequency(nbt, frequency);
		connections.saveNbt("connections", nbt);
		return nbt;
	}

	@Override
	public boolean isSideConnectable(Direction side)
	{
		return connections.get(side);
	}

	@Override
	public void toggleSideConnectable(Direction side)
	{
		connections.toggleBit(side);
		markDirty();


		var state = getCachedState();
		world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);

		var targetPos = pos.offset(side);
		world.replaceWithStateForNeighborUpdate(side.getOpposite(), state, targetPos, pos, Block.FORCE_STATE, 1);
		world.updateNeighbor(targetPos, state.getBlock(), pos);
	}

	@Override
	public void readNbt(NbtCompound nbt)
	{
		super.readNbt(nbt);
		frequency = WRUtils.readFrequency(nbt);
		connections = new SidedBitSet("connections", nbt);
	}

	@Override
	protected void writeNbt(NbtCompound nbt)
	{
		super.writeNbt(nbt);
		WRUtils.writeFrequency(nbt, frequency);
		connections.saveNbt("connections", nbt);
	}
}
