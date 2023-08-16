package rzk.wirelessredstone.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rzk.wirelessredstone.api.SelectedItemListener;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	public LivingEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	@Shadow
	protected ItemStack activeItemStack;

	@Inject(method = "clearActiveItem", at = @At("HEAD"))
	private void clearActiveItem(CallbackInfo ci)
	{
		World world = getWorld();
		ItemStack stack = activeItemStack;
		if (stack.getItem() instanceof SelectedItemListener listener)
			listener.onClearActiveItem(stack, world, (LivingEntity) (Object) this);
	}
}
