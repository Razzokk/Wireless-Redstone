package rzk.wirelessredstone.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;

public class WRCommands
{
	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event)
	{
		ClearFrequencyCommand.register(event.getDispatcher());
	}

	public static class ClearFrequencyCommand
	{
		public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
		{
			LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("wr")
					.requires((commandSource) -> commandSource.hasPermission(2))
					.then(Commands.literal("clear")
							.then(Commands.argument("frequency", IntegerArgumentType.integer(0, Short.toUnsignedInt(Short.MAX_VALUE)))
									.executes(ClearFrequencyCommand::clearFrequency))
							.then(Commands.literal("all")
									.executes(ClearFrequencyCommand::clearAll)));

			dispatcher.register(command);
		}

		private static int clearFrequency(CommandContext<CommandSourceStack> context)
		{
			CommandSourceStack sourceStack = context.getSource();
			ServerLevel world = sourceStack.getLevel();
			RedstoneNetwork network = RedstoneNetwork.get(world);
			short frequency = (short) IntegerArgumentType.getInteger(context, "frequency");

			if (!network.isChannelActive(frequency))
			{
				sourceStack.sendFailure(new TranslatableComponent(LangKeys.COMMAND_CLEAR_FAILURE));
				return 0;
			}

			network.clearFrequency(frequency);
			sourceStack.sendSuccess(new TranslatableComponent(LangKeys.COMMAND_CLEAR_SUCCESS).append(" " + frequency), true);
			return Command.SINGLE_SUCCESS;
		}

		private static int clearAll(CommandContext<CommandSourceStack> context)
		{
			CommandSourceStack sourceStack = context.getSource();
			ServerLevel world = sourceStack.getLevel();
			RedstoneNetwork.get(world).clearAll();
			sourceStack.sendSuccess(new TranslatableComponent(LangKeys.COMMAND_CLEAR_SUCCESS_ALL), true);
			return Command.SINGLE_SUCCESS;
		}
	}
}
