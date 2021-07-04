package rzk.wirelessredstone.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import rzk.wirelessredstone.util.LangKeys;
import rzk.wirelessredstone.util.WRConfig;

public class ScreenConfig extends Screen
{
	private static final int TITLE_HEIGHT = 8;
	private static final int TEXT_COLOR_WHITE = 0xffffff;
	private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
	private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
	private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;
	private static final int BUTTON_WIDTH = 200;
	private static final int RESET_BUTTON_WIDTH = 80;
	private static final int BUTTON_HEIGHT = 20;
	private static final int BUTTON_TOP_OFFSET = 26;
	private static final int MIN_COLOR = 0;
	private static final int MAX_COLOR = 255;
	private static final int STEPS_COLOR = 1;
	private OptionsRowList options;
	private Screen parent;

	public ScreenConfig(Screen parent)
	{
		super(new TranslationTextComponent(LangKeys.GUI_SETTINGS));
		this.parent = parent;
	}

	private static SliderPercentageOption colorSlider(String langKey, ForgeConfigSpec.IntValue valueHolder)
	{
		return new SliderPercentageOption(langKey, MIN_COLOR, MAX_COLOR, STEPS_COLOR,
				settings -> valueHolder.get().doubleValue(),
				(settings, newValue) -> valueHolder.set(newValue.intValue()),
				(settings, option) -> new TranslationTextComponent(langKey, (int) option.get(settings)));
	}

	@Override
	protected void init()
	{
		options = new OptionsRowList(minecraft, width, height, OPTIONS_LIST_TOP_HEIGHT,
				height - OPTIONS_LIST_BOTTOM_OFFSET, OPTIONS_LIST_ITEM_HEIGHT);

		options.addSmall(colorSlider(LangKeys.GUI_SETTINGS_DISPLAY_RED, WRConfig.FREQ_DISPLAY_COLOR_RED),
				colorSlider(LangKeys.GUI_SETTINGS_HIGHLIGHT_RED, WRConfig.HIGHLIGHT_COLOR_RED));

		options.addSmall(colorSlider(LangKeys.GUI_SETTINGS_DISPLAY_GREEN, WRConfig.FREQ_DISPLAY_COLOR_GREEN),
				colorSlider(LangKeys.GUI_SETTINGS_HIGHLIGHT_GREEN, WRConfig.HIGHLIGHT_COLOR_GREEN));

		options.addSmall(colorSlider(LangKeys.GUI_SETTINGS_DISPLAY_BLUE, WRConfig.FREQ_DISPLAY_COLOR_BLUE),
				colorSlider(LangKeys.GUI_SETTINGS_HIGHLIGHT_BLUE, WRConfig.HIGHLIGHT_COLOR_BLUE));

		options.addBig(new SliderPercentageOption(LangKeys.GUI_SETTINGS_HIGHLIGHT_TIME, 5, 500, 5,
				settings -> WRConfig.SNIFFER_HIGHLIGHT_TIME_CONF.get().doubleValue(),
				(settings, newValue) -> WRConfig.SNIFFER_HIGHLIGHT_TIME_CONF.set(newValue.intValue()),
				(settings, option) -> new TranslationTextComponent(LangKeys.GUI_SETTINGS_HIGHLIGHT_TIME, (int) option.get(settings))));

		children.add(this.options);

		addButton(new Button((width - BUTTON_WIDTH) / 2, height - BUTTON_TOP_OFFSET, BUTTON_WIDTH,
				BUTTON_HEIGHT, new TranslationTextComponent(LangKeys.GUI_DONE), button -> onClose()));

		addButton(new Button(width - RESET_BUTTON_WIDTH - 8, height - BUTTON_TOP_OFFSET, RESET_BUTTON_WIDTH,
				BUTTON_HEIGHT, new TranslationTextComponent(LangKeys.GUI_RESET), button -> onReset()));
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrixStack);
		options.render(matrixStack, mouseX, mouseY, partialTicks);
		drawCenteredString(matrixStack, font, title.getString(), width / 2, TITLE_HEIGHT, TEXT_COLOR_WHITE);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	@Override
	public void onClose()
	{
		WRConfig.save();
		minecraft.setScreen(parent);
	}

	private void onReset()
	{
		WRConfig.reset();
		init(minecraft, width, height);
	}
}
