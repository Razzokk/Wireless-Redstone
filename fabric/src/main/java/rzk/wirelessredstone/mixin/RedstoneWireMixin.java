package rzk.wirelessredstone.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import rzk.wirelessredstone.api.RedstoneConnectable;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireMixin
{
	@Shadow
	protected static boolean connectsTo(BlockState state, Direction dir)
	{
		return false;
	}

	@Redirect(method = "getRenderConnectionType(" +
		"Lnet/minecraft/world/BlockView;" +
		"Lnet/minecraft/util/math/BlockPos;" +
		"Lnet/minecraft/util/math/Direction;Z)" +
		"Lnet/minecraft/block/enums/WireConnection;",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/block/RedstoneWireBlock;" +
			"connectsTo(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z"))
	private boolean onConnectsTo(BlockState state, Direction side, BlockView world, BlockPos pos, Direction direction)
	{
		if (state.getBlock() instanceof RedstoneConnectable connectable)
			return connectable.connectsToRedstone(state, world, pos.offset(direction), side);
		return connectsTo(state, direction);
	}

	@Redirect(method = "getRenderConnectionType(" +
		"Lnet/minecraft/world/BlockView;" +
		"Lnet/minecraft/util/math/BlockPos;" +
		"Lnet/minecraft/util/math/Direction;Z)" +
		"Lnet/minecraft/block/enums/WireConnection;",
		at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/block/RedstoneWireBlock;" +
			"connectsTo(Lnet/minecraft/block/BlockState;)Z"))
	private boolean onConnectsToUp(BlockState state, BlockView world, BlockPos pos, Direction direction)
	{
		return onConnectsTo(state, null, world, pos.up(), direction);
	}

	@Redirect(method = "getRenderConnectionType(" +
		"Lnet/minecraft/world/BlockView;" +
		"Lnet/minecraft/util/math/BlockPos;" +
		"Lnet/minecraft/util/math/Direction;Z)" +
		"Lnet/minecraft/block/enums/WireConnection;",
		at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/block/RedstoneWireBlock;" +
			"connectsTo(Lnet/minecraft/block/BlockState;)Z"))
	private boolean onConnectsToDown(BlockState state, BlockView world, BlockPos pos, Direction direction)
	{
		return onConnectsTo(state, null, world, pos.down(), direction);
	}
}
