package rzk.wirelessredstone.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;
import rzk.wirelessredstone.generators.language.LanguageBase;
import rzk.wirelessredstone.misc.Utils;

import java.util.regex.Pattern;

@OnlyIn(Dist.CLIENT)
public abstract class FrequencyScreen extends Screen
{
	private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d*");
	private static final int WIDGET_WIDTH = 50;
	private static final int WIDGET_HEIGHT = 20;

	protected final int frequency;
	private EditBox frequencyInput;
	private Button done;
	private Button add1;
	private Button add10;
	private Button sub1;
	private Button sub10;

	protected FrequencyScreen(int frequency)
	{
		super(Component.translatable(LanguageBase.GUI_FREQUENCY_TITLE));
		this.frequency = frequency;
	}

	private Button addFrequencyButton(int x, int y, int value)
	{
		return addRenderableWidget(new Button(x, y, WIDGET_WIDTH, WIDGET_HEIGHT, Component.empty(), button ->
		{
			int frequency = 0;

			if (!frequencyInput.getValue().isBlank())
				frequency = getInputFrequency();

			frequency += value * (hasShiftDown() ? 100 : 1);
			frequency = Utils.clamp(Utils.MIN_FREQUENCY, Utils.MAX_FREQUENCY, frequency);

			frequencyInput.setValue(String.valueOf(frequency));
		}));
	}

	private void updateFrequencyButtonDesc()
	{
		if (hasShiftDown())
		{
			add1.setMessage(Component.literal("+100"));
			add10.setMessage(Component.literal("+1000"));
			sub1.setMessage(Component.literal("-100"));
			sub10.setMessage(Component.literal("-1000"));
		}
		else
		{
			add1.setMessage(Component.literal("+1"));
			add10.setMessage(Component.literal("+10"));
			sub1.setMessage(Component.literal("-1"));
			sub10.setMessage(Component.literal("-10"));
		}
	}

	@Override
	protected void init()
	{
		frequencyInput = addRenderableWidget(new EditBox(font, (width - 38) / 2, (height - 50) / 2, 38, WIDGET_HEIGHT, title));
		frequencyInput.setFilter(str -> DIGIT_PATTERN.matcher(str).matches());
		frequencyInput.setResponder(this::onFrequencyWritten);
		frequencyInput.setMaxLength(5);
		frequencyInput.setBordered(true);

		add1 = addFrequencyButton(frequencyInput.x + frequencyInput.getWidth() + 20, frequencyInput.y - WIDGET_HEIGHT / 2 - 2, 1);
		add10 = addFrequencyButton(frequencyInput.x + frequencyInput.getWidth() + 20, frequencyInput.y + WIDGET_HEIGHT / 2 + 2, 10);
		sub1 = addFrequencyButton(frequencyInput.x - WIDGET_WIDTH - 20, frequencyInput.y - WIDGET_HEIGHT / 2 - 2, -1);
		sub10 = addFrequencyButton(frequencyInput.x - WIDGET_WIDTH - 20, frequencyInput.y + WIDGET_HEIGHT / 2 + 2, -10);

		done = addRenderableWidget(new Button((width - WIDGET_WIDTH) / 2, frequencyInput.y + WIDGET_HEIGHT + 20, WIDGET_WIDTH, WIDGET_HEIGHT, Component.translatable("gui.done"), button ->
		{
			setFrequency();
			onClose();
		}));
		done.active = false;

		if (Utils.isValidFrequency(frequency))
			frequencyInput.setValue(String.valueOf(frequency));

		updateFrequencyButtonDesc();
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

		if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT)
		{
			updateFrequencyButtonDesc();
			return true;
		}

		return false;
	}

	@Override
	public boolean keyReleased(int keyCode, int unknown0, int unknown1)
	{
		if (super.keyPressed(keyCode, unknown0, unknown1))
			return true;

		if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT)
		{
			updateFrequencyButtonDesc();
			return true;
		}

		return false;
	}

	@Override
	public void render(PoseStack poseStack, int p_96563_, int p_96564_, float p_96565_)
	{
		renderBackground(poseStack);
		super.render(poseStack, p_96563_, p_96564_, p_96565_);
		drawCenteredString(poseStack, font, getTitle(), width / 2, frequencyInput.y - 30, 0xFFFFFF);
	}

	private void onFrequencyWritten(String str)
	{
		done.active = str.length() > 0;
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}

	protected int getInputFrequency()
	{
		return Integer.parseInt(frequencyInput.getValue());
	}

	protected abstract void setFrequency();
}
