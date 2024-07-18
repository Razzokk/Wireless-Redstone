package rzk.wirelessredstone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import rzk.wirelessredstone.api.ChunkLoadListener;
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
		.entries((displayContext, entries) -> {
			entries.add(ModBlocks.redstoneTransmitter);
			entries.add(ModBlocks.redstoneReceiver);
			entries.add(ModBlocks.p2pRedstoneTransmitter);
			entries.add(ModBlocks.p2pRedstoneReceiver);
			entries.add(ModItems.circuit);
			entries.add(ModItems.frequencyTool);
			entries.add(ModItems.frequencySniffer);
			entries.add(ModItems.remote);
			entries.add(ModItems.linker);
			entries.add(ModItems.debugger);
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

		ServerChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
			var blockEntities = chunk.getBlockEntities().values();

			for (var blockEntity : blockEntities)
				if (blockEntity instanceof ChunkLoadListener be)
					be.onChunkLoad(world);
		});

		ServerChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> {
			var blockEntities = chunk.getBlockEntities().values();

			for (var blockEntity : blockEntities)
				if (blockEntity instanceof ChunkLoadListener be)
					be.onChunkUnload(world);
		});
	}
}
