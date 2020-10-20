package rzk.wirelessredstone.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import rzk.lib.mc.util.WorldUtils;
import rzk.lib.util.ObjectUtils;
import rzk.wirelessredstone.RedstoneNetwork;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.client.LangKeys;
import rzk.wirelessredstone.tile.TileFrequency;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWirelessRemote extends ItemFrequency
{
	private static final IItemPropertyGetter POWERED = (stack, world, entity) ->
			stack.getOrCreateTag().getBoolean("powered") ? 1.0F : 0.0F;

	public ItemWirelessRemote()
	{
		//addPropertyOverride(new ResourceLocation("powered"), POWERED);
	}

	public void setPowered(World world, ItemStack stack, boolean powered)
	{
		RedstoneNetwork network = RedstoneNetwork.getOrCreate(world);

		if (network != null)
		{
			int frequency = getFrequency(stack);
			if (powered)
				network.addActiveTransmitter(frequency);
			else
				network.removeActiveTransmitter(frequency);
		}

		CompoundNBT compound = stack.getOrCreateTag();
		compound.putBoolean("powered", powered);
		stack.setTag(compound);
	}

	public boolean isPowered(ItemStack stack)
	{
		return stack.getOrCreateTag().getBoolean("powered");
	}

	@Override
	public void setFrequency(World world, ItemStack stack, int frequency)
	{
		if (!world.isRemote && isPowered(stack))
			RedstoneNetwork.getOrCreate(world).changeActiveTransmitterFrequency(getFrequency(stack), frequency);
		super.setFrequency(world, stack, frequency);
	}

	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
	{
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		if (world.getBlockState(pos).getBlock() instanceof BlockFrequency && context.getPlayer().isSneaking())
		{
			if (!world.isRemote)
				WorldUtils.ifTilePresent(world, pos, TileFrequency.class, tile -> setFrequency(world, stack, tile.getFrequency()));
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (!player.isSneaking() && !isPowered(stack))
		{
			setPowered(world, stack, true);
			player.setActiveHand(hand);
			return ActionResult.resultConsume(stack);
		}
		return super.onItemRightClick(world, player, hand);
	}

	@Override
	public int getUseDuration(ItemStack stack)
	{
		return 72000;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entityLiving, int timeLeft)
	{
		if (isPowered(stack))
			setPowered(world, stack, false);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
	{
		if (!isSelected && isPowered(stack))
			setPowered(world, stack, false);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		super.addInformation(stack, worldIn, tooltip, flag);

		ITextComponent stateText = new TranslationTextComponent(LangKeys.Tooltip.STATE).mergeStyle(TextFormatting.GRAY);
		stateText.getSiblings().add(new StringTextComponent(": "));

		boolean isPowered = isPowered(stack);
		ITextComponent state = new TranslationTextComponent(isPowered ? LangKeys.Tooltip.ON : LangKeys.Tooltip.OFF)
				.mergeStyle(isPowered ? TextFormatting.GREEN : TextFormatting.DARK_RED);

		stateText.getSiblings().add(state);

		tooltip.add(stateText);
	}
}
