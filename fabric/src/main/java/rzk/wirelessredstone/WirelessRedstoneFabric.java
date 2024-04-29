package rzk.wirelessredstone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.registry.ModBlockEntitiesFabric;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModBlocksFabric;
import rzk.wirelessredstone.registry.ModItems;
import rzk.wirelessredstone.registry.ModItemsFabric;
import rzk.wirelessredstone.registry.ModNetworking;

public class WirelessRedstoneFabric implements ModInitializer
{
	private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
		.entries((displayContext, entries) ->
		{
			entries.add(ModBlocks.redstoneTransmitter);
			entries.add(ModBlocks.redstoneReceiver);
			entries.add(ModItems.circuit);
			entries.add(ModItems.frequencyTool);
			entries.add(ModItems.frequencySniffer);
			entries.add(ModItems.remote);
		})
		.displayName(Text.translatable(TranslationKeys.ITEM_GROUP_WIRELESS_REDSTONE))
		.icon(() -> new ItemStack(ModBlocks.redstoneTransmitter))
		.build();

	@Override
	public void onInitialize()
	{
		WRConfig.load();

		ModBlocksFabric.registerBlocks();
		ModItemsFabric.registerItems();
		ModBlockEntitiesFabric.registerBlockEntities();
		ModNetworking.register();

		Registry.register(Registries.ITEM_GROUP, WirelessRedstone.identifier(WirelessRedstone.MODID), ITEM_GROUP);
	}
}
