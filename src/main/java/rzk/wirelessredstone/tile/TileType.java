package rzk.wirelessredstone.tile;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.function.Supplier;

public final class TileType<T extends TileEntity> extends TileEntityType<T>
{
	private final boolean hasValidBlocks;

	public TileType(Supplier<? extends T> factory)
	{
		super(factory, null, null);
		hasValidBlocks = false;
	}

	public TileType(Supplier<? extends T> factory, Block... validBlocks)
	{
		super(factory, validBlocks == null ? null : ImmutableSet.copyOf(validBlocks), null);
		hasValidBlocks = validBlocks != null && validBlocks.length > 0;
	}

	@Override
	public boolean isValid(Block block)
	{
		if (hasValidBlocks)
			return super.isValid(block);
		return true;
	}
}
