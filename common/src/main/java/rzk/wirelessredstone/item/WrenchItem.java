package rzk.wirelessredstone.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import rzk.wirelessredstone.api.SideConnectable;

public class WrenchItem extends Item
{
	public WrenchItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx)
	{
		var world = ctx.getWorld();
		var pos = ctx.getBlockPos();
		var state = world.getBlockState(pos);

		if (!(state.getBlock() instanceof SideConnectable connectable))
			return super.useOnBlock(ctx);

		connectable.toggleSideConnectable(state, world, pos, ctx.getSide());
		return ActionResult.SUCCESS;
	}
}
