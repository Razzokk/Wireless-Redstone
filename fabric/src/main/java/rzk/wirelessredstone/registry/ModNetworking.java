package rzk.wirelessredstone.registry;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import rzk.wirelessredstone.block.RedstoneTransceiverBlock;
import rzk.wirelessredstone.item.FrequencyItem;
import rzk.wirelessredstone.item.RemoteItem;
import rzk.wirelessredstone.network.FrequencyBlockPacket;
import rzk.wirelessredstone.network.FrequencyItemPacket;

public class ModNetworking
{
	public static void register()
	{
		ServerPlayNetworking.registerGlobalReceiver(FrequencyBlockPacket.TYPE, (packet, player, responseSender) ->
		{
			World world = player.getWorld();
			if (world.getBlockState(packet.pos()).getBlock() instanceof RedstoneTransceiverBlock block)
				block.setFrequency(world, packet.pos(), packet.frequency());
		});

		ServerPlayNetworking.registerGlobalReceiver(FrequencyItemPacket.TYPE, (packet, player, responseSender) ->
		{
			ItemStack stack = player.getStackInHand(packet.hand());
			if (stack.getItem() instanceof FrequencyItem item)
				item.setFrequency(stack, packet.frequency());
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
		{
			ServerPlayerEntity player = handler.player;
			ItemStack stack = player.getActiveItem();
			if (!stack.isOf(ModItems.remote)) return;
			((RemoteItem) stack.getItem()).onDeactivation(stack, player.getWorld(), player);
		});
	}
}
