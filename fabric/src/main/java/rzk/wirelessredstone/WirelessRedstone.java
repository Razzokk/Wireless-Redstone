package rzk.wirelessredstone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rzk.wirelessredstone.block.ModBlocks;
import rzk.wirelessredstone.block.entity.ModBlockEntities;
import rzk.wirelessredstone.item.ModItems;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.network.ModNetworking;

public class WirelessRedstone implements ModInitializer
{
	public static final String MODID = "wirelessredstone";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(identifier(MODID),
			() -> ModBlocks.REDSTONE_TRANSMITTER.asItem().getDefaultStack());

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
	}
}
