package rzk.wirelessredstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import rzk.wirelessredstone.block.entity.RedstoneTransceiverBlockEntity;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.misc.WRUtils;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.ModNetworking;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public abstract class RedstoneTransceiverBlock extends Block implements EntityBlock
{
	public RedstoneTransceiverBlock()
	{
		super(Properties.of(Material.METAL)
			.isRedstoneConductor((state, level, pos) -> false)
			.strength(1.5F, 5.0F)
			.sound(SoundType.METAL));
		registerDefaultState(stateDefinition.any().setValue(POWERED, false));
	}

	public void setFrequency(Level level, BlockPos pos, int frequency)
	{
		if (WRUtils.isValidFrequency(frequency) && level.getBlockEntity(pos) instanceof RedstoneTransceiverBlockEntity transceiver)
			transceiver.setFrequency(frequency);
	}

	public int getFrequency(Level level, BlockPos pos)
	{
		if (level.getBlockEntity(pos) instanceof RedstoneTransceiverBlockEntity transceiver)
			return transceiver.getFrequency();
		return 0;
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction)
	{
		return true;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
	{
		if (player.getItemInHand(hand).getItem() instanceof FrequencyItem)
			return InteractionResult.PASS;

		if (!level.isClientSide)
			ModNetworking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
				new FrequencyBlockPacket.OpenScreen(getFrequency(level, pos), pos));

		return InteractionResult.SUCCESS;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(POWERED);
	}
}
