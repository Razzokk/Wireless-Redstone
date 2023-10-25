package rzk.wirelessredstone;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import rzk.wirelessredstone.screen.ConfigScreen;

public class WRModMenu implements ModMenuApi
{
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory()
	{
		return ConfigScreen::create;
	}
}
