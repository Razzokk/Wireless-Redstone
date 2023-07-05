package rzk.wirelessredstone.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import rzk.wirelessredstone.datagen.DefaultLanguageGenerator;
import rzk.wirelessredstone.misc.WRUtils;

import java.util.regex.Pattern;

@Environment(EnvType.CLIENT)
public abstract class FrequencyScreen extends Screen
{
	private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d*");
	private static final int WIDGET_WIDTH = 50;
	private static final int WIDGET_HEIGHT = 20;

	protected final int frequency;
	private TextFieldWidget frequencyInput;
	private ButtonWidget done;
	private ButtonWidget add1;
	private ButtonWidget add10;
	private ButtonWidget sub1;
	private ButtonWidget sub10;

	protected FrequencyScreen(int frequency)
	{
		super(Text.translatable(DefaultLanguageGenerator.GUI_FREQUENCY_TITLE));
		this.frequency = frequency;
	}

	private ButtonWidget addFrequencyButton(int x, int y, int value)
	{
		return addDrawableChild(ButtonWidget.builder(Text.empty(), button ->
		{
			int frequency = 0;

			if (!frequencyInput.getText().isBlank())
				frequency = getInputFrequency();

			frequency += value * (hasShiftDown() ? 100 : 1);
			frequency = WRUtils.clamp(WRUtils.MIN_FREQUENCY, WRUtils.MAX_FREQUENCY, frequency);

			frequencyInput.setText(String.valueOf(frequency));
		}).position(x, y).size(WIDGET_WIDTH, WIDGET_HEIGHT).build());
	}

	private void updateFrequencyButtonDesc()
	{
		if (hasShiftDown())
		{
			add1.setMessage(Text.literal("+100"));
			add10.setMessage(Text.literal("+1000"));
			sub1.setMessage(Text.literal("-100"));
			sub10.setMessage(Text.literal("-1000"));
		}
		else
		{
			add1.setMessage(Text.literal("+1"));
			add10.setMessage(Text.literal("+10"));
			sub1.setMessage(Text.literal("-1"));
			sub10.setMessage(Text.literal("-10"));
		}
	}

	@Override
	protected void init()
	{
		frequencyInput = addDrawableChild(new TextFieldWidget(textRenderer, (width - 38) / 2, (height - 50) / 2, 38, WIDGET_HEIGHT, title));
		frequencyInput.setTextPredicate(str -> DIGIT_PATTERN.matcher(str).matches());
		frequencyInput.setChangedListener(this::onFrequencyWritten);
		frequencyInput.setMaxLength(5);

		add1 = addFrequencyButton(frequencyInput.getX() + frequencyInput.getWidth() + 20, frequencyInput.getY() - WIDGET_HEIGHT / 2 - 2, 1);
		add10 = addFrequencyButton(frequencyInput.getX() + frequencyInput.getWidth() + 20, frequencyInput.getY() + WIDGET_HEIGHT / 2 + 2, 10);
		sub1 = addFrequencyButton(frequencyInput.getX() - WIDGET_WIDTH - 20, frequencyInput.getY() - WIDGET_HEIGHT / 2 - 2, -1);
		sub10 = addFrequencyButton(frequencyInput.getX() - WIDGET_WIDTH - 20, frequencyInput.getY() + WIDGET_HEIGHT / 2 + 2, -10);

		done = addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), button ->
		{
			setFrequency();
			close();
		}).position((width - WIDGET_WIDTH) / 2, frequencyInput.getY() + WIDGET_HEIGHT + 20).size(WIDGET_WIDTH, WIDGET_HEIGHT).build());
		done.active = false;

		if (WRUtils.isValidFrequency(frequency))
			frequencyInput.setText(String.valueOf(frequency));

		updateFrequencyButtonDesc();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if (super.keyPressed(keyCode, scanCode, modifiers))
			return true;

		if (keyCode == GLFW.GLFW_KEY_E)
		{
			close();
			return true;
		}

		if (keyCode == GLFW.GLFW_KEY_ENTER && !frequencyInput.getText().isBlank())
		{
			setFrequency();
			close();
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
	public boolean keyReleased(int keyCode, int scanCode, int modifiers)
	{
		if (super.keyReleased(keyCode, scanCode, modifiers))
			return true;

		if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT)
		{
			updateFrequencyButtonDesc();
			return true;
		}

		return false;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		renderBackground(context);
		super.render(context, mouseX, mouseY, delta);
		context.drawText(textRenderer, title, (width - textRenderer.getWidth(title)) / 2, frequencyInput.getY() - 30, 0xFFFFFF, false);
	}

	private void onFrequencyWritten(String str)
	{
		done.active = str.length() > 0;
	}

	@Override
	public boolean shouldPause()
	{
		return false;
	}

	protected int getInputFrequency()
	{
		return Integer.parseInt(frequencyInput.getText());
	}

	protected abstract void setFrequency();
}
