package rzk.lib.mc.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.lib.util.ObjectUtils;

import java.util.function.Consumer;

public final class WorldUtils
{
	public static <T> T getTile(World world, BlockPos pos, Class<T> clazz)
	{
		return ObjectUtils.cast(world.getTileEntity(pos), clazz);
	}

	public static <T> boolean ifTilePresent(World world, BlockPos pos, Class<T> clazz, Consumer<T> consumer)
	{
		return ObjectUtils.ifCastable(world.getTileEntity(pos), clazz, consumer);
	}
}
