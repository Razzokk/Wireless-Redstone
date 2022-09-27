package rzk.wirelessredstone.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.IFixableData;
import rzk.wirelessredstone.WirelessRedstone;

public class WRTEDataFix implements IFixableData
{
	@Override
	public int getFixVersion()
	{
		return 0;
	}

	@Override
	public NBTTagCompound fixTagCompound(NBTTagCompound nbt)
	{
		String id = nbt.getString("id");
		String oldId = new ResourceLocation(WirelessRedstone.MOD_ID, "tile.tile_frequency").toString();

		if (oldId.equals(id))
		{
			nbt.setString("id", new ResourceLocation(WirelessRedstone.MOD_ID, "tileentity." +
					(nbt.getBoolean("type") ? "transmitter" : "receiver")).toString());
			nbt.removeTag("type");
		}

		return nbt;
	}
}
