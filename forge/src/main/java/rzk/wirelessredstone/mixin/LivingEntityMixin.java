package rzk.wirelessredstone.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rzk.wirelessredstone.api.SelectedItemListener;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	public LivingEntityMixin(EntityType<?> entityType, Level level)
	{
		super(entityType, level);
	}

	@Shadow
	protected ItemStack useItem;

	@Inject(method = "stopUsingItem", at = @At("HEAD"))
	private void stopUsingItem(CallbackInfo ci)
	{
		Level level = getLevel();
		ItemStack stack = useItem;
		if (stack.getItem() instanceof SelectedItemListener listener)
			listener.onClearActiveItem(stack, level, (LivingEntity) (Object) this);
	}
}
