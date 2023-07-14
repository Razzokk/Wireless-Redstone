package rzk.wirelessredstone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rzk.wirelessredstone.block.ModBlocks;
import rzk.wirelessredstone.block.entity.ModBlockEntities;
import rzk.wirelessredstone.item.ModItems;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.network.ModNetworking;

public class WirelessRedstone implements ModInitializer
{
	public static final String MODID = "wirelessredstone";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
		.entries((displayContext, entries) ->
		{
			entries.add(ModBlocks.REDSTONE_TRANSMITTER);
			entries.add(ModBlocks.REDSTONE_RECEIVER);
			entries.add(ModItems.CIRCUIT);
			entries.add(ModItems.FREQUENCY_TOOL);
			entries.add(ModItems.FREQUENCY_SNIFFER);
			entries.add(ModItems.REMOTE);
		})
		.displayName(Text.translatable(TranslationKeys.ITEM_GROUP_WIRELESS_REDSTONE))
		.icon(() -> new ItemStack(ModBlocks.REDSTONE_TRANSMITTER))
		.build();

	public static Identifier identifier(String path)
	{
		return new Identifier(MODID, path);
	}

	@Override
	public void onInitialize()
	{
		WRConfig.load();

		ModBlocks.registerBlocks();
		ModItems.registerItems();
		ModBlockEntities.registerBlockEntities();
		ModNetworking.register();

		Registry.register(Registries.ITEM_GROUP, identifier(MODID), ITEM_GROUP);
	}
}
