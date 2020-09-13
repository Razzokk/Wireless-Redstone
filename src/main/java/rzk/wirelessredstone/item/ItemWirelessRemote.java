package rzk.wirelessredstone.item;

import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import rzk.lib.mc.util.WorldUtils;
import rzk.wirelessredstone.RedstoneNetwork;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.tile.TileFrequency;

import javax.annotation.Nullable;
import java.util.List;

public class ItemWirelessRemote extends ItemFrequency
{
	private static final IItemPropertyGetter POWERED_GETTER = (stack, world, entity) ->
			stack.getOrCreateTag().getBoolean("powered") ? 1.0F : 0.0F;

	public ItemWirelessRemote()
	{
		addPropertyOverride(new ResourceLocation("powered"), POWERED_GETTER);
	}

	private void setPowered(World world, ItemStack stack, boolean powered)
	{
		RedstoneNetwork network = RedstoneNetwork.getOrCreate(world);
		int frequency = getFrequency(stack);

		if (network != null)
		{
			if (powered)
				network.addActiveTransmitter(frequency, world);
			else
				network.removeActiveTransmitter(frequency, world);
		}

		CompoundNBT compound = stack.getOrCreateTag();
		compound.putBoolean("powered", powered);
		stack.setTag(compound);
	}

	private boolean isPowered(ItemStack stack)
	{
		return stack.getOrCreateTag().getBoolean("powered");
	}

	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
	{
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		if (world.getBlockState(pos).getBlock() instanceof BlockFrequency && context.getPlayer().isSneaking())
		{
			if (!world.isRemote)
				WorldUtils.ifTilePresent(world, pos, TileFrequency.class, tile -> setFrequency(stack, tile.getFrequency()));
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);

		if (player.isSneaking())
		{
			if (world.isRemote)
				WirelessRedstone.proxy.openRemoteGui(getFrequency(stack), hand);
			return ActionResult.resultSuccess(stack);
		}
		else if (!player.isSneaking())
		{
			setPowered(world, stack, !isPowered(stack));
			return ActionResult.resultConsume(stack);
		}

		return ActionResult.resultPass(stack);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flag)
	{
		super.addInformation(stack, worldIn, tooltip, flag);

		boolean isPowered = isPowered(stack);
		ITextComponent state = new TranslationTextComponent("tooltip." + WirelessRedstone.MODID + "." + (isPowered ? "on" : "off"));
		state.applyTextStyle(isPowered ? TextFormatting.GREEN : TextFormatting.DARK_RED);

		ITextComponent stateText = new TranslationTextComponent("tooltip." + WirelessRedstone.MODID + ".state");
		stateText.appendText(": ");
		stateText.applyTextStyle(TextFormatting.GRAY);
		stateText.appendSibling(state);

		tooltip.add(stateText);
	}
}
