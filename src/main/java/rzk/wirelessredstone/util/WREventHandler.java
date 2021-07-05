package rzk.wirelessredstone.util;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.item.ItemRemote;
import rzk.wirelessredstone.registry.ModBlocks;
import rzk.wirelessredstone.registry.ModItems;

public class WREventHandler
{
	private static void powerOffRemote(World world, ItemStack stack)
	{
		if (!world.isClientSide && stack.getItem() instanceof ItemRemote && ItemRemote.isPowered(stack))
			ItemRemote.setPowered(world, stack, false);
	}

	@SubscribeEvent
	public static void onPlayerToss(ItemTossEvent event)
	{
		powerOffRemote(event.getPlayer().level, event.getEntityItem().getItem());
	}

	@SubscribeEvent
	public static void onPlayerDrops(LivingDropsEvent event)
	{
		LivingEntity entity = event.getEntityLiving();
		powerOffRemote(entity.level, entity.getUseItem());
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		PlayerEntity player = event.getPlayer();
		powerOffRemote(player.level, player.getUseItem());
	}

	// For backwards compatibility
	@SubscribeEvent
	public static void onMissingBlockMappings(RegistryEvent.MissingMappings<Block> event)
	{
		for (RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getAllMappings())
		{
			if (mapping.key.getNamespace().equals(WirelessRedstone.MOD_ID))
			{
				WirelessRedstone.hasMissingBlockMappings = true;
				switch (mapping.key.getPath())
				{
					case "wireless_transmitter":
						mapping.remap(ModBlocks.redstoneTransmitter);
						break;
					case "wireless_receiver":
						mapping.remap(ModBlocks.redstoneReceiver);
						break;
				}
			}
		}
	}

	// For backwards compatibility
	@SubscribeEvent
	public static void onMissingItemMappings(RegistryEvent.MissingMappings<Item> event)
	{
		for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getAllMappings())
		{
			if (mapping.key.getNamespace().equals(WirelessRedstone.MOD_ID))
			{
				switch (mapping.key.getPath())
				{
					case "wireless_transmitter":
						mapping.remap(ModBlocks.redstoneTransmitter.asItem());
						break;
					case "wireless_receiver":
						mapping.remap(ModBlocks.redstoneReceiver.asItem());
						break;
					case "wireless_circuit":
						mapping.remap(ModItems.circuit);
						break;
					case "wireless_remote":
						mapping.remap(ModItems.remote);
						break;
					case "frequency_copier":
						mapping.remap(ModItems.frequencyTool);
						break;
				}
			}
		}
	}
}
