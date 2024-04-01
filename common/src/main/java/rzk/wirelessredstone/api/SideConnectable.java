package rzk.wirelessredstone.api;

import net.minecraft.util.math.Direction;

public interface SideConnectable
{
	/**
	 * Check if side of block is connectable.
	 *
	 * @param side The side of the to check
	 * @return <c>true</c> if the side is connectable, <c>false</c> otherwise
	 */
	boolean isSideConnectable(Direction side);

	/**
	 * Toggle connectable side of block.
	 *
	 * @param side The side of the to set
	 */
	void toggleSideConnectable(Direction side);
}
