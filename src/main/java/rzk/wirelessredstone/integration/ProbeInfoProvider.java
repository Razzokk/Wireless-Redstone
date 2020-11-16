package rzk.wirelessredstone.integration;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import net.minecraft.world.World;

public interface ProbeInfoProvider
{
	void addProbeInfo(IProbeInfo info, World world, IProbeHitData data);
}
