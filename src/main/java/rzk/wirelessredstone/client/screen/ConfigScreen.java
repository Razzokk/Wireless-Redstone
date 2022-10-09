package rzk.wirelessredstone.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import rzk.wirelessredstone.generator.language.LanguageBase;
import rzk.wirelessredstone.misc.Config;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends Screen
{
	private static final int TITLE_HEIGHT = 8;
	private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
	private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
	private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;
	private static final int BUTTON_WIDTH = 150;
	private static final int RESET_BUTTON_WIDTH = 120;
	private static final int BUTTON_HEIGHT = 20;
	private static final int BUTTON_TOP_OFFSET = 26;
	private OptionsList options;
	private final Screen parent;

	private final List<ValueHolder<?>> VALUE_HOLDERS = new ArrayList<>();
	private final ValueHolder<Integer> SIGNAL_STRENGTH = new ValueHolder<>(VALUE_HOLDERS, Config.REDSTONE_RECEIVER_SIGNAL_STRENGTH);
	private final ValueHolder<Boolean> STRONG_POWER = new ValueHolder<>(VALUE_HOLDERS, Config.REDSTONE_RECEIVER_STRONG_POWER);
	private final ValueHolder<Integer> DISPLAY_COLOR_RED = new ValueHolder<>(VALUE_HOLDERS, Config.FREQUENCY_DISPLAY_COLOR_RED);
	private final ValueHolder<Integer> DISPLAY_COLOR_GREEN = new ValueHolder<>(VALUE_HOLDERS, Config.FREQUENCY_DISPLAY_COLOR_GREEN);
	private final ValueHolder<Integer> DISPLAY_COLOR_BLUE = new ValueHolder<>(VALUE_HOLDERS, Config.FREQUENCY_DISPLAY_COLOR_BLUE);
	private final ValueHolder<Integer> HIGHLIGHT_COLOR_RED = new ValueHolder<>(VALUE_HOLDERS, Config.HIGHLIGHT_COLOR_RED);
	private final ValueHolder<Integer> HIGHLIGHT_COLOR_GREEN = new ValueHolder<>(VALUE_HOLDERS, Config.HIGHLIGHT_COLOR_GREEN);
	private final ValueHolder<Integer> HIGHLIGHT_COLOR_BLUE = new ValueHolder<>(VALUE_HOLDERS, Config.HIGHLIGHT_COLOR_BLUE);
	private final ValueHolder<Integer> HIGHLIGHT_TIME_SECONDS = new ValueHolder<>(VALUE_HOLDERS, Config.HIGHLIGHT_TIME_SECONDS);

	public ConfigScreen(Screen parent)
	{
		super(Component.translatable(LanguageBase.GUI_CONFIG_TITLE));
		this.parent = parent;
	}

	private static OptionInstance<Integer> slider(String langKey, ValueHolder<Integer> valueHolder, int min, int max)
	{
		return new OptionInstance<>(langKey, OptionInstance.noTooltip(), Options::genericValueLabel,
				new OptionInstance.IntRange(min, max), valueHolder.value, valueHolder::set);
	}

	private static OptionInstance<Boolean> button(String langKey, ValueHolder<Boolean> valueHolder)
	{
		return OptionInstance.createBoolean(langKey, valueHolder.value, valueHolder::set);
	}

	@Override
	protected void init()
	{
		options = new OptionsList(minecraft, width, height, OPTIONS_LIST_TOP_HEIGHT,
				height - OPTIONS_LIST_BOTTOM_OFFSET, OPTIONS_LIST_ITEM_HEIGHT);

		options.addSmall(new OptionInstance[]
				{
						slider(LanguageBase.GUI_CONFIG_SIGNAL_STRENGTH, SIGNAL_STRENGTH, 1, 15),
						button(LanguageBase.GUI_CONFIG_STRONG_POWER, STRONG_POWER),
						slider(LanguageBase.GUI_CONFIG_DISPLAY_RED, DISPLAY_COLOR_RED, 0, 255),
						slider(LanguageBase.GUI_CONFIG_HIGHLIGHT_RED, HIGHLIGHT_COLOR_RED, 0, 255),
						slider(LanguageBase.GUI_CONFIG_DISPLAY_GREEN, DISPLAY_COLOR_GREEN, 0, 255),
						slider(LanguageBase.GUI_CONFIG_HIGHLIGHT_GREEN, HIGHLIGHT_COLOR_GREEN, 0, 255),
						slider(LanguageBase.GUI_CONFIG_DISPLAY_BLUE, DISPLAY_COLOR_BLUE, 0, 255),
						slider(LanguageBase.GUI_CONFIG_HIGHLIGHT_BLUE, HIGHLIGHT_COLOR_BLUE, 0, 255)
				});

		options.addBig(slider(LanguageBase.GUI_CONFIG_HIGHLIGHT_TIME, HIGHLIGHT_TIME_SECONDS, 1, 500));

		addRenderableWidget(options);

		addRenderableWidget(new Button((width - BUTTON_WIDTH) / 2, height - BUTTON_TOP_OFFSET, BUTTON_WIDTH,
				BUTTON_HEIGHT, Component.translatable("gui.done"), button -> onClose()));

		addRenderableWidget(new Button(width - RESET_BUTTON_WIDTH - 8, height - BUTTON_TOP_OFFSET, RESET_BUTTON_WIDTH,
				BUTTON_HEIGHT, Component.translatable("controls.reset"), button -> onReset()));
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		drawCenteredString(poseStack, font, title, width / 2, TITLE_HEIGHT, 0xFFFFFF);
	}

	@Override
	public void onClose()
	{
		for (ValueHolder<?> holder : VALUE_HOLDERS)
			holder.save();
		Config.updateInternals();
		minecraft.setScreen(parent);
	}

	private void onReset()
	{
		for (ValueHolder<?> holder : VALUE_HOLDERS)
			holder.reset();
		init(minecraft, width, height);
	}

	public static class ValueHolder<T>
	{
		private ForgeConfigSpec.ConfigValue<T> holder;
		private T value;

		public ValueHolder(List<ValueHolder<?>> holders, ForgeConfigSpec.ConfigValue<T> holder)
		{
			this.holder = holder;
			value = holder.get();
			holders.add(this);
		}

		public void set(T value)
		{
			this.value = value;
		}

		public void reset()
		{
			this.value = holder.getDefault();
		}

		public void save()
		{
			holder.set(value);
		}
	}
}
