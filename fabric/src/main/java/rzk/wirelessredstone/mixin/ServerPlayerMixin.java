package rzk.wirelessredstone.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
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
	public abstract ServerWorld getWorld();

	public ServerPlayerMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey)
	{
		super(world, pos, yaw, gameProfile, publicKey);
	}

	@Inject(method = "dropSelectedItem", at = @At("HEAD"))
	public void dropSelectedItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir)
	{
		ItemStack stack = getMainHandStack();
		if (stack.isEmpty()) return;

		if (stack.getItem() instanceof SelectedItemListener listener)
		{
			ItemStack copy = stack.copy();
			copy.setCount(1);
			listener.onSelectedItemDropped(entireStack ? stack : copy, getWorld(), (ServerPlayerEntity) (Object) this);
		}
	}
}
