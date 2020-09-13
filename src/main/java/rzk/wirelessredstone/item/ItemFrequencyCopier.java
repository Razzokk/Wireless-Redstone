package rzk.wirelessredstone.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.lib.mc.util.WorldUtils;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.tile.TileFrequency;

public class ItemFrequencyCopier extends ItemFrequency
{
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
	{
		World world = context.getWorld();
		BlockPos pos = context.getPos();

		if (world.getBlockState(pos).getBlock() instanceof BlockFrequency)
		{
			if (!world.isRemote)
			{
				WorldUtils.ifTilePresent(world, pos, TileFrequency.class, tile ->
				{
					if (context.getPlayer().isSneaking())
						setFrequency(stack, tile.getFrequency());
					else
						tile.setFrequency(getFrequency(stack));
				});
			}
			return ActionResultType.SUCCESS;
		}

		return ActionResultType.PASS;
	}
}
