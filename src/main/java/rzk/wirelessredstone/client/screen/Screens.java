package rzk.wirelessredstone.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;

public class Screens
{
	public static void openFrequencyBlockScreen(int frequency, BlockPos pos)
	{
		Minecraft.getInstance().setScreen(new FrequencyBlockScreen(frequency, pos));
	}

	public static void openFrequencyItemScreen(int frequency, InteractionHand hand)
	{
		Minecraft.getInstance().setScreen(new FrequencyItemScreen(frequency, hand));
	}
}
