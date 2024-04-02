package rzk.wirelessredstone.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import rzk.wirelessredstone.api.Connectable;

public class WrenchItem extends Item
{
	public WrenchItem(Settings settings)
	{
		super(settings.maxCount(1));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx)
	{
		var world = ctx.getWorld();
		var pos = ctx.getBlockPos();

		if (!(world.getBlockEntity(pos) instanceof Connectable connectable))
			return super.useOnBlock(ctx);

		if (!world.isClient)
			connectable.toggleConnectable(ctx.getSide());

		return ActionResult.SUCCESS;
	}
}
