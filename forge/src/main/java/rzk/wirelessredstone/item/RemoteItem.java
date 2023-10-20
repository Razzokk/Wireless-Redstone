package rzk.wirelessredstone.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRUtils;

public class RemoteItem extends FrequencyItem
{
	public RemoteItem(Properties properties)
	{
		super(properties);
	}

	public void onDeactivation(ItemStack stack, Level level, LivingEntity owner)
	{
		if (!level.isClientSide)
		{
			RedstoneEther ether = RedstoneEther.get((ServerLevel) level);
			if (ether == null) return;
			int frequency = getFrequency(stack);
			ether.removeRemote(level, owner, frequency);
		}
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		if (context.getPlayer().isShiftKeyDown()) return super.useOn(context);
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		if (player.isShiftKeyDown()) return super.use(level, player, hand);

		ItemStack stack = player.getItemInHand(hand);
		int frequency = getFrequency(stack);
		player.getCooldowns().addCooldown(this, 10);

		if (!WRUtils.isValidFrequency(frequency))
		{
			if (!level.isClientSide)
				player.displayClientMessage(Component.translatable(TranslationKeys.MESSAGE_NO_FREQUENCY).withStyle(ChatFormatting.RED), true);
			return InteractionResultHolder.consume(stack);
		}

		player.startUsingItem(hand);

		if (!level.isClientSide)
		{
			RedstoneEther ether = RedstoneEther.getOrCreate((ServerLevel) level);
			ether.addRemote(level, player, frequency);
		}

		return InteractionResultHolder.sidedSuccess(stack, false);
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack stack, Player player)
	{
		Level level = player.level();
		if (!level.isClientSide && player.getUseItem().is(this))
			onDeactivation(stack, level, player);
		return true;
	}

	@Override
	public void onStopUsing(ItemStack stack, LivingEntity entity, int count)
	{
		Level level = entity.level();
		if (!level.isClientSide)
			onDeactivation(stack, level, entity);
	}
}
