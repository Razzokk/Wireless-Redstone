package rzk.wirelessredstone.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRUtils;
import rzk.wirelessredstone.network.FrequencyItemPacket;
import rzk.wirelessredstone.network.ModNetworking;

import java.util.List;

public class FrequencyItem extends Item
{
	public FrequencyItem(Properties properties)
	{
		super(properties.stacksTo(1));
	}

	public int getFrequency(ItemStack stack)
	{
		return WRUtils.readFrequency(stack.getTag());
	}

	public void setFrequency(ItemStack stack, int frequency)
	{
		WRUtils.writeFrequency(stack.getOrCreateTag(), frequency);
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();

		if (level.getBlockState(pos).getBlock() instanceof RedstoneTransceiverBlock transceiver)
		{
			Player player = context.getPlayer();
			ItemStack stack = context.getItemInHand();
			boolean isShift = player.isShiftKeyDown();

			int frequency = isShift ? transceiver.getFrequency(level, pos) : getFrequency(stack);

			if (!WRUtils.isValidFrequency(frequency))
				return InteractionResult.FAIL;

			if (isShift) setFrequency(stack, frequency);
			else transceiver.setFrequency(level, pos, frequency);

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

		if (!level.isClientSide)
			ModNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
				new FrequencyItemPacket.OpenScreen(getFrequency(stack), hand));

		return InteractionResultHolder.success(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced)
	{
		int frequency = getFrequency(stack);
		if (!WRUtils.isValidFrequency(frequency)) return;

		Component frequencyComponent = Component.literal(String.valueOf(frequency)).withStyle(ChatFormatting.AQUA);
		tooltip.add(Component.translatable(TranslationKeys.TOOLTIP_FREQUENCY, frequencyComponent).withStyle(ChatFormatting.GRAY));
	}
}
