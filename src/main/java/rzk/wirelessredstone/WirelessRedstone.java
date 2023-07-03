package rzk.wirelessredstone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rzk.wirelessredstone.block.ModBlocks;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.block.entity.ModBlockEntities;
import rzk.wirelessredstone.datagen.DefaultLanguageGenerator;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.item.ModItems;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;

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
		.displayName(Text.translatable(DefaultLanguageGenerator.ITEM_GROUP_WIRELESS_REDSTONE))
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
		Registry.register(Registries.ITEM_GROUP, identifier(MODID), ITEM_GROUP);

		ServerPlayNetworking.registerGlobalReceiver(FrequencyBlockPacket.TYPE, (packet, player, responseSender) ->
		{
			World world = player.getWorld();
			if (world.getBlockState(packet.pos).getBlock() instanceof RedstoneTransceiverBlock block)
				block.setFrequency(world, packet.pos, packet.frequency);
		});

		ServerPlayNetworking.registerGlobalReceiver(FrequencyItemPacket.TYPE, (packet, player, responseSender) ->
		{
			ItemStack stack = player.getStackInHand(packet.hand);
			if (stack.getItem() instanceof FrequencyItem item)
				item.setFrequency(stack, packet.frequency);
		});
	}
}
