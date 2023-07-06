package rzk.wirelessredstone.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rzk.wirelessredstone.api.SelectedItemListener;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerMixin extends PlayerEntity
{
	@Shadow
	public abstract ServerWorld getServerWorld();

	public ServerPlayerMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile)
	{
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "dropSelectedItem", at = @At("HEAD"))
	public void dropSelectedItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir)
	{
		ItemStack stack = getMainHandStack();
		if (stack.isEmpty()) return;

		if (stack.getItem() instanceof SelectedItemListener listener)
			listener.onSelectedItemDropped(entireStack ? stack : stack.copyWithCount(1), getServerWorld(), (ServerPlayerEntity) (Object) this);
	}
}
