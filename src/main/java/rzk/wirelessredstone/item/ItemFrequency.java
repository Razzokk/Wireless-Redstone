package rzk.wirelessredstone.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.client.screen.ClientScreens;
import rzk.wirelessredstone.registry.ModItems;
import rzk.wirelessredstone.rsnetwork.Device;
import rzk.wirelessredstone.tile.TileFrequency;
import rzk.wirelessredstone.util.LangKeys;

import javax.annotation.Nullable;
import java.util.List;

public class ItemFrequency extends Item
{
	public ItemFrequency()
	{
		super(ModItems.defaultItemProperties().stacksTo(1));
	}

	public static short getFrequency(ItemStack stack)
	{
		if (stack.getItem() instanceof ItemFrequency)
			return stack.getOrCreateTag().getShort("frequency");

		return 0;
	}

	public static void setFrequency(ItemStack stack, short frequency)
	{
		if (stack.getItem() instanceof ItemFrequency)
			stack.getOrCreateTag().putShort("frequency", frequency);
	}



	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand)
	{
		if (!player.isShiftKeyDown())
			return super.use(world, player, hand);

		ItemStack stack = player.getItemInHand(hand);

		if (world.isClientSide && stack.getItem() instanceof ItemFrequency)
			ClientScreens.openFreqGui(Device.createRemote(getFrequency(stack), hand));

		return InteractionResultHolder.success(stack);
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
	{
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();

		if (!BlockFrequency.isFreqBlock(world.getBlockState(pos)))
			return InteractionResult.PASS;

		if (!world.isClientSide && world.getBlockEntity(pos) instanceof TileFrequency tile)
		{
			Player player = context.getPlayer();

			if (player.isShiftKeyDown())
				setFrequency(stack, tile.getFrequency());
			else
				tile.setFrequency(getFrequency(stack));
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag tooltipFlag)
	{
		Component frequency = new TextComponent(String.valueOf(Short.toUnsignedInt(getFrequency(stack)))).withStyle(ChatFormatting.AQUA);
		Component tooltip = new TranslatableComponent(LangKeys.TOOLTIP_FREQUENCY, frequency).withStyle(ChatFormatting.GRAY);
		list.add(tooltip);
	}
}
