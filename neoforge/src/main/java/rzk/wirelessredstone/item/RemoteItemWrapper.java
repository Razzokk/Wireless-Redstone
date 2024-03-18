package rzk.wirelessredstone.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class RemoteItemWrapper extends RemoteItem
{
	public RemoteItemWrapper(Settings settings)
	{
		super(settings);
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, PlayerEntity player)
	{
		World world = player.getWorld();
		if (world.isClient) return true;

		onSelectedItemDropped(item, (ServerWorld) world, (ServerPlayerEntity) player);
		return true;
	}

	@Override
	public void onStopUsing(ItemStack stack, LivingEntity entity, int count)
	{
		onClearActiveItem(stack, entity.getWorld(), entity);
	}
}
