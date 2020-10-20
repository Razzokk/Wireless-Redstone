package rzk.lib.mc.registry;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import rzk.lib.mc.item.IItemProvider;
import rzk.lib.mc.tile.TileType;

import java.util.List;

public final class ModRegistry
{
	public static Block registerBlock(String modid, List<Block> blocks, List<Item> items, Block block, String name)
	{
		blocks.add(block.setRegistryName(modid, name));
		if (block instanceof IItemProvider)
		{
			BlockItem item = ((IItemProvider) block).createItem();
			if (item != IItemProvider.NO_ITEM)
				registerItem(modid, items, item, name);
		}
		return block;
	}

	public static Item registerItem(String modid, List<Item> items, Item item, String name)
	{
		items.add(item.setRegistryName(modid, name));
		return item;
	}

	public static TileEntityType<?> registerTile(String modid, List<TileEntityType<?>> tiles, TileEntityType<?> tile, String name)
	{
		tiles.add(tile.setRegistryName(modid, name));
		return tile;
	}
}
