package rzk.wirelessredstone.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;

public class ModScreens
{
	public static void openBlockFrequencyScreen(int frequency, BlockPos pos)
	{
		Minecraft.getInstance().setScreen(new FrequencyBlockScreen(frequency, pos));
	}

	public static void openItemFrequencyScreen(int frequency, InteractionHand hand)
	{
		Minecraft.getInstance().setScreen(new FrequencyItemScreen(frequency, hand));
	}
}
