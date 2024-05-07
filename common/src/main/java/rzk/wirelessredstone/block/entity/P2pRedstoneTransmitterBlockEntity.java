package rzk.wirelessredstone.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import rzk.wirelessredstone.misc.NbtKeys;
import rzk.wirelessredstone.registry.ModBlockEntities;
import rzk.wirelessredstone.registry.ModBlocks;

import static net.minecraft.state.property.Properties.POWERED;
import static rzk.wirelessredstone.misc.WRProperties.LINKED;

public class P2pRedstoneTransmitterBlockEntity extends BlockEntity
{
	private BlockPos target = null;

	public P2pRedstoneTransmitterBlockEntity(BlockPos pos, BlockState state)
	{
		super(ModBlockEntities.p2pRedstoneTransmitterBlockEntityType, pos, state);
	}

	public void link(BlockPos target)
	{
		this.target = target;
		markDirty();

		var state = getCachedState();
		world.setBlockState(pos, state.with(LINKED, true), Block.NOTIFY_LISTENERS);
		setTargetState(state.get(POWERED));
	}

	public void setTargetState(boolean powered)
	{
		if (world.isClient || target == null) return;

		var targetState = world.getBlockState(target);
		if (!targetState.isOf(ModBlocks.p2pRedstoneReceiver) || (targetState.get(POWERED) == powered)) return;

		world.setBlockState(target, targetState.with(POWERED, powered));
	}

	@Override
	public void readNbt(NbtCompound nbt)
	{
		super.readNbt(nbt);
		var targetNbt = nbt.getCompound(NbtKeys.LINKER_TARGET);
		if (targetNbt != null)
			target = NbtHelper.toBlockPos(targetNbt);
	}

	@Override
	protected void writeNbt(NbtCompound nbt)
	{
		super.writeNbt(nbt);
		if (target != null)
			nbt.put(NbtKeys.LINKER_TARGET, NbtHelper.fromBlockPos(target));
	}
}
