package rzk.wirelessredstone.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.misc.NbtKeys;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRUtils;

import java.util.List;

public class LinkerItem extends Item
{
	public LinkerItem(Settings settings)
	{
		super(settings);
	}

	private static void setTarget(ItemStack stack, BlockPos pos)
	{
		var nbt = NbtHelper.fromBlockPos(pos);
		stack.setSubNbt(NbtKeys.LINKER_TARGET, nbt);
	}

	public static BlockPos getTarget(ItemStack stack)
	{
		var nbt = stack.getSubNbt(NbtKeys.LINKER_TARGET);
		if (nbt == null) return null;
		return NbtHelper.toBlockPos(nbt);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		var player = context.getPlayer();
		if (!player.isSneaking()) return super.useOnBlock(context);

		if (!context.getWorld().isClient)
			setTarget(context.getStack(), context.getBlockPos());

		return ActionResult.SUCCESS;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
	{
		var target = getTarget(stack);
		if (target == null) return;

		var targetText = WRUtils.positionText(target);
		tooltip.add(Text.translatable(TranslationKeys.TOOLTIP_TARGET, targetText).formatted(Formatting.GRAY));
	}
}
