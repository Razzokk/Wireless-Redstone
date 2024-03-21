package rzk.wirelessredstone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import rzk.wirelessredstone.block.ModBlocks;
import rzk.wirelessredstone.block.ModBlocksFabric;
import rzk.wirelessredstone.block.entity.ModBlockEntitiesFabric;
import rzk.wirelessredstone.item.ModItems;
import rzk.wirelessredstone.item.ModItemsFabric;
import rzk.wirelessredstone.misc.TranslationKeys;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.network.ModNetworking;

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
			entries.add(ModItems.wrench);
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
