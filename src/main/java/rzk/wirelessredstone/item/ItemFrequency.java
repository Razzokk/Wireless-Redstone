package rzk.wirelessredstone.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rzk.wirelessredstone.block.BlockFrequency;
import rzk.wirelessredstone.tile.TileFrequency;

public class ItemFrequency extends Item
{
	private short getFrequency(ItemStack stack)
	{
		if (!stack.hasTagCompound())
			return 0;

		return stack.getTagCompound().getShort("frequency");
	}

	private void setFrequency(ItemStack stack, short frequency)
	{
		NBTTagCompound nbt;

		if (stack.hasTagCompound())
		{
			nbt = stack.getTagCompound();
		}
		else
		{
			nbt = new NBTTagCompound();
			stack.setTagCompound(nbt);
		}

		nbt.setShort("frequency", frequency);
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if (world.getBlockState(pos).getBlock() instanceof BlockFrequency)
		{
			if (!world.isRemote)
			{
				TileEntity tile = world.getTileEntity(pos);

				if (tile instanceof TileFrequency)
				{
					ItemStack stack = player.getHeldItem(hand);

					if (player.isSneaking())
						setFrequency(stack, ((TileFrequency) tile).getFrequency());
					else
						((TileFrequency) tile).setFrequency(getFrequency(stack));
				}
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
}
