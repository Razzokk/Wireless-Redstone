package rzk.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.api.RedstoneConnectable;
import rzk.wirelessredstone.api.SideConnectable;
import rzk.wirelessredstone.block.entity.RedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.item.WrenchItem;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRUtils;

import java.util.List;

import static net.minecraft.state.property.Properties.POWERED;

public abstract class RedstoneTransceiverBlock extends Block implements BlockEntityProvider, RedstoneConnectable
{
	public RedstoneTransceiverBlock()
	{
		super(Settings.create()
			.mapColor(MapColor.IRON_GRAY)
			.solidBlock((state, blockGetter, pos) -> false)
			.strength(1.5F, 5.0F)
			.sounds(BlockSoundGroup.METAL));

		setDefaultState(stateManager.getDefaultState().with(POWERED, false));
	}

	public void setFrequency(World world, BlockPos pos, int frequency)
	{
		if (WRUtils.isValidFrequency(frequency) && world.getBlockEntity(pos) instanceof RedstoneTransceiverBlockEntity transceiver)
			transceiver.setFrequency(frequency);
	}

	public int getFrequency(World world, BlockPos pos)
	{
		if (world.getBlockEntity(pos) instanceof RedstoneTransceiverBlockEntity transceiver)
			return transceiver.getFrequency();
		return 0;
	}

	@Override
	public boolean connectsToRedstone(BlockState state, BlockView world, BlockPos pos, Direction direction)
	{
		if (direction == null || !(world.getBlockEntity(pos) instanceof SideConnectable connectable)) return false;
		return connectable.isSideConnectable(direction.getOpposite());
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		var item = player.getStackInHand(hand).getItem();

		if (item instanceof FrequencyItem || item instanceof WrenchItem)
			return ActionResult.PASS;

		if (!world.isClient)
			WirelessRedstone.PLATFORM.sendFrequencyBlockPacket((ServerPlayerEntity) player, getFrequency(world, pos), pos);

		return ActionResult.SUCCESS;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(POWERED);
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options)
	{
		var frequency = WRUtils.readFrequency(BlockItem.getBlockEntityNbt(stack));
		if (!WRUtils.isValidFrequency(frequency)) return;

		Text frequencyComponent = Text.literal(String.valueOf(frequency)).formatted(Formatting.AQUA);
		tooltip.add(Text.translatable(TranslationKeys.TOOLTIP_FREQUENCY, frequencyComponent).formatted(Formatting.GRAY));
	}
}
