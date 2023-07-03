package rzk.wirelessredstone.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.datagen.DefaultLanguageGenerator;

import java.util.List;

public class RemoteItem extends FrequencyItem
{
	public RemoteItem(Settings settings)
	{
		super(settings);
	}

	public static boolean isOn(ItemStack stack)
	{
		if (!stack.hasNbt() || !stack.getNbt().contains("state"))
			return false;
		return stack.getNbt().getBoolean("state");
	}

	public static void setOn(ItemStack stack, boolean state)
	{
		NbtCompound nbt = stack.getOrCreateNbt();
		nbt.putBoolean("state", state);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand)
	{
		if (player.isSneaking()) return super.use(world, player, hand);

		player.getItemCooldownManager().set(this, 10);

		if (!world.isClient)
		{
			ItemStack stack = player.getStackInHand(hand);
			boolean state = !isOn(stack);
			setOn(stack, state);
		}

		return super.use(world, player, hand);
	}

	@Override
	public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference)
	{
		if (clickType == ClickType.LEFT) return false;
		if (player.getItemCooldownManager().isCoolingDown(this)) return true;

		player.getItemCooldownManager().set(this, 10);
		World world = player.getWorld();

		if (!world.isClient)
			setOn(stack, !isOn(stack));

		return true;
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
	{
		super.appendTooltip(stack, world, tooltip, context);

		boolean mode = true;
		Text modeText = Text.translatable(mode ? DefaultLanguageGenerator.TOOLTIP_MODE_TOGGLE : DefaultLanguageGenerator.TOOLTIP_MODE_SWITCH)
			.formatted(Formatting.LIGHT_PURPLE);
		tooltip.add(Text.translatable(DefaultLanguageGenerator.TOOLTIP_MODE, modeText).formatted(Formatting.GRAY));

		boolean on = isOn(stack);
		Text state = Text.translatable(on ? DefaultLanguageGenerator.TOOLTIP_STATE_ON : DefaultLanguageGenerator.TOOLTIP_STATE_OFF)
			.formatted(on ? Formatting.GREEN : Formatting.RED);
		tooltip.add(Text.translatable(DefaultLanguageGenerator.TOOLTIP_STATE, state).formatted(Formatting.GRAY));
	}
}
