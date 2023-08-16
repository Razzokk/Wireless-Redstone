package rzk.wirelessredstone.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import rzk.wirelessredstone.api.SelectedItemListener;
import rzk.wirelessredstone.ether.RedstoneEther;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRUtils;

public class RemoteItem extends FrequencyItem implements SelectedItemListener
{
	public RemoteItem(Settings settings)
	{
		super(settings);
	}

	public void onDeactivation(ItemStack stack, World world, LivingEntity owner)
	{
		if (!world.isClient)
		{
			RedstoneEther ether = RedstoneEther.get((ServerWorld) world);
			if (ether == null) return;
			int frequency = getFrequency(stack);
			ether.removeRemote(world, owner, frequency);
		}
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context)
	{
		if (context.getPlayer().isSneaking()) return super.useOnBlock(context);
		return ActionResult.PASS;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		if (player.isSneaking()) return super.use(world, player, hand);

		ItemStack stack = player.getStackInHand(hand);
		int frequency = getFrequency(stack);
		player.getItemCooldownManager().set(this, 10);

		if (!WRUtils.isValidFrequency(frequency))
		{
			if (!world.isClient)
				player.sendMessage(Text.translatable(TranslationKeys.MESSAGE_NO_FREQUENCY).formatted(Formatting.RED));
			return TypedActionResult.consume(stack);
		}

		player.setCurrentHand(hand);

		if (!world.isClient)
		{
			RedstoneEther ether = RedstoneEther.getOrCreate((ServerWorld) world);
			ether.addRemote(world, player, frequency);
		}

		return TypedActionResult.success(stack, false);
	}

	@Override
	public int getMaxUseTime(ItemStack stack)
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public void onSelectedItemDropped(ItemStack stack, ServerWorld world, ServerPlayerEntity player)
	{
		if (!player.getActiveItem().isEmpty())
			onDeactivation(stack, world, player);
	}

	@Override
	public void onClearActiveItem(ItemStack stack, World world, LivingEntity entity)
	{
		if (!world.isClient && !entity.getActiveItem().isEmpty())
			onDeactivation(stack, world, entity);
	}
}
