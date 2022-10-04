package rzk.wirelessredstone.client.screen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public abstract class FrequencyScreen extends Screen
{
	protected final int frequency;

	protected FrequencyScreen(int frequency)
	{
		super(Component.translatable("screen.frequency"));
		this.frequency = frequency;
	}

	@Override
	protected void init()
	{
		addRenderableWidget(new Button(width / 2, height / 2, 200, 20, Component.translatable("button.done"), button ->
		{
			setFrequency();
			onClose();
		}));
	}

	@Override
	public boolean keyPressed(int keyCode, int unknown0, int unknown1)
	{
		if (super.keyPressed(keyCode, unknown0, unknown1))
			return true;

		if (keyCode == GLFW.GLFW_KEY_E)
		{
			onClose();
			return true;
		}

		return false;
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}

	protected abstract void setFrequency();
}
