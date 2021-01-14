package rzk.wirelessredstone.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.tile.TileFrequency;
import rzk.wirelessredstone.util.DeviceType;

import java.util.Random;

public class ItemDebug extends Item
{
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		if (!world.isRemote)
		{
			Random rand = new Random();

			for (int x = 0; x < 100; x++)
			{
				for (int z = 0; z < 100; z++)
				{
					BlockFrequency block = (BlockFrequency) (rand.nextBoolean() ? ModBlocks.transmitter : ModBlocks.receiver);
					ItemBlock item = (ItemBlock) Item.getItemFromBlock(block);
					BlockPos pos = block.type == DeviceType.TRANSMITTER ? player.getPosition().add(x, -2, z) : player.getPosition().add(x, 2, z);
					item.placeBlockAt(item.getDefaultInstance(), player, world, pos, null, 0, 0, 0, block.getDefaultState());
					TileEntity tile = world.getTileEntity(pos);

					if (tile instanceof TileFrequency)
						((TileFrequency) tile).setFrequency((short) rand.nextInt(65000));

					if (block.type == DeviceType.TRANSMITTER && rand.nextBoolean())
						world.setBlockState(pos.add(0, -1, 0), Blocks.REDSTONE_BLOCK.getDefaultState());
				}
			}
		}
		return super.onItemRightClick(world, player, hand);
	}
}
