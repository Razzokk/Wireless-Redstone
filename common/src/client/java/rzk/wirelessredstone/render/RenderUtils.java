package rzk.wirelessredstone.render;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;

public class RenderUtils
{
	public static void drawLine(BufferBuilder builder, MatrixStack.Entry matrix, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float red, float green, float blue, float alpha)
	{
		var lenX = maxX - minX;
		var lenY = maxY - minY;
		var lenZ = maxZ - minZ;
		var len = MathHelper.sqrt(lenX * lenX + lenY * lenY + lenZ * lenZ);

		lenX /= len;
		lenY /= len;
		lenZ /= len;

		builder.vertex(matrix.getPositionMatrix(), minX, minY, minZ)
			.color(red, green, blue, alpha)
			.normal(matrix.getNormalMatrix(), lenX, lenY, lenZ)
			.next();

		builder.vertex(matrix.getPositionMatrix(), maxX, maxY, maxZ)
			.color(red, green, blue, alpha)
			.normal(matrix.getNormalMatrix(), lenX, lenY, lenZ)
			.next();
	}

	public static void drawOutlineShape(BufferBuilder builder, MatrixStack.Entry matrix, VoxelShape shape, BlockPos pos, float red, float green, float blue, float alpha)
	{
		shape.forEachEdge((minX, minY, minZ, maxX, maxY, maxZ) ->
			drawLine(builder, matrix,
				(float) (pos.getX() + minX),
				(float) (pos.getY() + minY),
				(float) (pos.getZ() + minZ),
				(float) (pos.getX() + maxX),
				(float) (pos.getY() + maxY),
				(float) (pos.getZ() + maxZ),
				red, green, blue, alpha)
		);
	}
}
