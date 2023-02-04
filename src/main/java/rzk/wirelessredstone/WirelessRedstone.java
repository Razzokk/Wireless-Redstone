package rzk.wirelessredstone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rzk.wirelessredstone.block.ModBlocks;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.blockentity.ModBlockEntities;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.item.ModItems;
import rzk.wirelessredstone.misc.WRConfig;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;

public class WirelessRedstone implements ModInitializer
{
	public static final String MODID = "wirelessredstone";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder(identifier(MODID))
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

		ServerPlayNetworking.registerGlobalReceiver(FrequencyBlockPacket.ID, (server, player, handler, buf, responseSender) ->
		{
			FrequencyBlockPacket packet = new FrequencyBlockPacket(buf);
			server.execute(() ->
			{
				World world = player.world;

				if (world.getBlockState(packet.pos).getBlock() instanceof RedstoneTransceiverBlock block)
					block.setFrequency(world, packet.pos, packet.frequency);
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(FrequencyItemPacket.ID, (server, player, handler, buf, responseSender) ->
		{
			FrequencyItemPacket packet = new FrequencyItemPacket(buf);
			server.execute(() ->
			{
				ItemStack stack = player.getStackInHand(packet.hand);

				if (stack.getItem() instanceof FrequencyItem item)
					item.setFrequency(stack, packet.frequency);
			});
		});

		ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP).register(content ->
		{
			content.add(ModBlocks.REDSTONE_TRANSMITTER);
			content.add(ModBlocks.REDSTONE_RECEIVER);
			content.add(ModItems.CIRCUIT);
			content.add(ModItems.FREQUENCY_TOOL);
			content.add(ModItems.FREQUENCY_SNIFFER);
		});
	}
}
