package rzk.wirelessredstone.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.network.PacketFrequencyOpenGui;
import rzk.wirelessredstone.network.PacketHandler;
import rzk.wirelessredstone.registry.ModItems;
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
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		if (!player.isShiftKeyDown())
			return super.use(world, player, hand);

		ItemStack stack = player.getItemInHand(hand);

		if (!world.isClientSide && stack.getItem() instanceof ItemFrequency)
		{
			boolean extended = player.getPersistentData().getBoolean(WirelessRedstone.MOD_ID + ".extended");
			PacketHandler.sendToPlayer(new PacketFrequencyOpenGui(getFrequency(stack), extended, hand), (ServerPlayerEntity) player);
		}

		return ActionResult.success(stack);
	}

	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context)
	{
		World world = context.getLevel();
		BlockPos pos = context.getClickedPos();

		if (!BlockFrequency.isFreqBlock(world.getBlockState(pos)))
			return ActionResultType.PASS;

		if (!world.isClientSide)
		{
			TileEntity tile = world.getBlockEntity(pos);

			if (tile instanceof TileFrequency)
			{
				PlayerEntity player = context.getPlayer();
				TileFrequency tileFrequency = (TileFrequency) tile;

				if (player.isShiftKeyDown())
					setFrequency(stack, tileFrequency.getFrequency());
				else
					tileFrequency.setFrequency(getFrequency(stack));
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag tooltipFlag)
	{
		ITextComponent frequency = new StringTextComponent(String.valueOf(Short.toUnsignedInt(getFrequency(stack)))).withStyle(TextFormatting.AQUA);
		ITextComponent tooltip = new TranslationTextComponent(LangKeys.TOOLTIP_FREQUENCY, frequency).withStyle(TextFormatting.GRAY);
		list.add(tooltip);
	}
}
