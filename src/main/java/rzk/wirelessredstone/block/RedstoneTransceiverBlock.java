package rzk.wirelessredstone.block;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.wirelessredstone.block.entity.RedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.misc.WRUtils;
import rzk.wirelessredstone.network.FrequencyBlockPacket;

import static net.minecraft.state.property.Properties.POWERED;

public abstract class RedstoneTransceiverBlock extends Block implements BlockEntityProvider
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
	public boolean emitsRedstonePower(BlockState state)
	{
		return true;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if (player.getStackInHand(hand).getItem() instanceof FrequencyItem)
			return ActionResult.PASS;

		if (!world.isClient)
			ServerPlayNetworking.send((ServerPlayerEntity) player, new FrequencyBlockPacket(getFrequency(world, pos), pos));

		return ActionResult.SUCCESS;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(POWERED);
	}
}
