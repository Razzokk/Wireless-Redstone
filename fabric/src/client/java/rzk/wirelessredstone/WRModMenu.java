package rzk.wirelessredstone;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRConfig;

public class WRModMenu implements ModMenuApi
{
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory()
	{
		return parent ->
		{
			ConfigBuilder builder = ConfigBuilder.create()
				.setTitle(Text.translatable(TranslationKeys.GUI_CONFIG_TITLE))
				.setParentScreen(parent)
				.setSavingRunnable(WRConfig::save);

			ConfigCategory general = builder.getOrCreateCategory(Text.translatable(TranslationKeys.GUI_CONFIG_CATEGORY_GENERAL));

			general.addEntry(ConfigEntryBuilder.create()
				.startIntSlider(Text.translatable(TranslationKeys.GUI_CONFIG_SIGNAL_STRENGTH), WRConfig.redstoneReceiverSignalStrength, 1, 15)
				.setDefaultValue(15)
				.setSaveConsumer(strength -> WRConfig.redstoneReceiverSignalStrength = strength)
				.build());

			general.addEntry(ConfigEntryBuilder.create()
				.startBooleanToggle(Text.translatable(TranslationKeys.GUI_CONFIG_STRONG_POWER), WRConfig.redstoneReceiverStrongPower)
				.setDefaultValue(true)
				.setSaveConsumer(strongPower -> WRConfig.redstoneReceiverStrongPower = strongPower)
				.build());

			ConfigCategory client = builder.getOrCreateCategory(Text.translatable(TranslationKeys.GUI_CONFIG_CATEGORY_CLIENT));

			client.addEntry(ConfigEntryBuilder.create()
				.startColorField(Text.translatable(TranslationKeys.GUI_CONFIG_DISPLAY_COLOR), WRConfig.frequencyDisplayColor)
				.setDefaultValue(0)
				.setSaveConsumer(color -> WRConfig.frequencyDisplayColor = color)
				.build());

			client.addEntry(ConfigEntryBuilder.create()
				.startColorField(Text.translatable(TranslationKeys.GUI_CONFIG_HIGHLIGHT_COLOR), WRConfig.highlightColor)
				.setDefaultValue(0xFF3F3F)
				.setSaveConsumer(color -> WRConfig.highlightColor = color)
				.build());

			client.addEntry(ConfigEntryBuilder.create()
				.startIntField(Text.translatable(TranslationKeys.GUI_CONFIG_HIGHLIGHT_TIME), WRConfig.highlightTimeSeconds)
				.setDefaultValue(10)
				.setMin(1)
				.setSaveConsumer(seconds -> WRConfig.highlightTimeSeconds = seconds)
				.build());

			return builder.build();
		};
	}
}
