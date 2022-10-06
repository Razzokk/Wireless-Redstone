package rzk.wirelessredstone.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.blocks.RedstoneTransceiverBlock;
import rzk.wirelessredstone.client.screen.Screens;
import rzk.wirelessredstone.misc.Utils;

import java.util.List;

public class FrequencyItem extends Item
{
	public FrequencyItem(Properties props)
	{
		super(props);
	}

	public int getFrequency(ItemStack stack)
	{
		return Utils.readFrequency(stack.getTag());
	}

	public void setFrequency(ItemStack stack, int frequency)
	{
		Utils.writeFrequency(stack.getOrCreateTag(), frequency);
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
	{
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();

		if (level.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock transceiver)
		{
			Player player = context.getPlayer();
			boolean isShift = player.isShiftKeyDown();

			int frequency = isShift ? transceiver.getFrequency(level, pos) : getFrequency(stack);

			if (!Utils.isValidFrequency(frequency))
				return InteractionResult.FAIL;

			if (isShift)
				setFrequency(stack, frequency);
			else
				transceiver.setFrequency(level, pos, frequency);

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);

		if (!player.isShiftKeyDown())
			return InteractionResultHolder.pass(stack);

		if (level.isClientSide)
			Screens.openFrequencyItemScreen(getFrequency(stack), hand);

		return InteractionResultHolder.success(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag)
	{
		int frequency = getFrequency(stack);
		if (!Utils.isValidFrequency(frequency)) return;

		MutableComponent frequencyComponent = Component.literal(String.valueOf(frequency)).withStyle(ChatFormatting.AQUA);
		list.add(Component.translatable("item.frequency.tooltip %s", frequencyComponent).withStyle(ChatFormatting.GRAY));
	}
}
