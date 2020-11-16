package rzk.wirelessredstone.integration;

import mcjty.theoneprobe.api.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.InterModComms;
import rzk.wirelessredstone.WirelessRedstone;

import java.util.function.Function;

public class TOPIntegration
{
	private static boolean registered = false;

	public static void register()
	{
		if (registered)
			return;
		registered = true;
		InterModComms.sendTo("theoneprobe", "getTheOneProbe", GetProbe::new);
	}

	public static class GetProbe implements Function<ITheOneProbe, Void>
	{
		@Override
		public Void apply(ITheOneProbe probe)
		{
			probe.registerProvider(new IProbeInfoProvider()
			{
				@Override
				public String getID()
				{
					return WirelessRedstone.MOD_ID;
				}

				@Override
				public void addProbeInfo(ProbeMode mode, IProbeInfo info, PlayerEntity player, World world, BlockState state, IProbeHitData data)
				{
					if (state.getBlock() instanceof ProbeInfoProvider)
					{
						ProbeInfoProvider provider = (ProbeInfoProvider) state.getBlock();
						provider.addProbeInfo(info, world, data);
					}
				}

			});
			return null;
		}
	}
}
