package rzk.lib.mc.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

@FunctionalInterface
public interface IItemProvider
{
	BlockItem provideItem(Block block);
}
