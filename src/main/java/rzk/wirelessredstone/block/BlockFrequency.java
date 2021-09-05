package rzk.wirelessredstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import rzk.wirelessredstone.client.screen.ClientScreens;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.rsnetwork.Device;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;
import rzk.wirelessredstone.tile.TileFrequency;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockFrequency extends BlockRedstoneDevice implements EntityBlock
{
	private final Device.Type type;

	public BlockFrequency(Device.Type type)
	{
		super(Properties.of(Material.METAL).strength(0.5F, 5.0F).sound(SoundType.METAL));
		this.type = type;
	}

	@Override
	public float defaultDestroyTime()
	{
		return super.defaultDestroyTime();
	}

	public static boolean isFreqBlock(BlockState state)
	{
		return state.is(ModBlocks.redstoneReceiver) || state.is(ModBlocks.redstoneTransmitter);
	}

	public static void setPoweredState(Level world, BlockPos pos, boolean powered)
	{
		BlockState state = world.getBlockState(pos);

		if (isFreqBlock(state))
			((BlockFrequency) state.getBlock()).setPowered(state, world, pos, powered);
	}

	@Override
	protected boolean isInputSide(BlockState state, Direction side)
	{
		return isTransmitter();
	}

	@Override
	protected boolean isOutputSide(BlockState state, Direction side)
	{
		return isReceiver();
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
	{
		if (world.isClientSide && world.getBlockEntity(pos) instanceof Device.Block device)
			ClientScreens.openFreqGui(device);

		return InteractionResult.SUCCESS;
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		if (!world.isClientSide)
			world.getBlockTicks().scheduleTick(pos, this, 1);
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random rand)
	{
		if (!world.isClientSide)
		{
			RedstoneNetwork network = RedstoneNetwork.get(world);
			BlockEntity tile = world.getBlockEntity(pos);

			if (isTransmitter() && isGettingPowered(state, world, pos))
			{
				setPowered(state, world, pos, true);
				if (tile instanceof TileFrequency)
					network.addDevice((TileFrequency) tile);
			}
			else if (isReceiver() && tile instanceof TileFrequency)
			{
				network.addDevice((TileFrequency) tile);
			}
		}
	}

	@Override
	protected void onInputChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighbour, Direction side)
	{
		if (!world.isClientSide && isTransmitter() && shouldUpdate(state, world, pos))
		{
			boolean powered = isGettingPowered(state, world, pos);
			setPowered(state, world, pos, powered);

			if (world.getBlockEntity(pos) instanceof Device device)
			{
				RedstoneNetwork network = RedstoneNetwork.get((ServerLevel) world);

				if (network != null)
				{
					if (powered)
						network.addDevice(device);
					else
						network.removeDevice(device);
				}
			}
		}
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
	{
		if (!level.isClientSide && level.getBlockEntity(pos) instanceof TileFrequency tile)
			RedstoneNetwork.get((ServerLevel) level).removeDevice(tile);

		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return switch (type)
		{
			case TRANSMITTER -> new TileFrequency.Transmitter(pos, state);
			case RECEIVER -> new TileFrequency.Receiver(pos, state);
			default -> throw new IllegalStateException("Unexpected value: " + type + " for frequency block");
		};
	}

	public boolean isTransmitter()
	{
		return type == Device.Type.TRANSMITTER;
	}

	public boolean isReceiver()
	{
		return type == Device.Type.RECEIVER;
	}
}
