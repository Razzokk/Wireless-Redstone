package rzk.wirelessredstone.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.impl.FillCommand;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;

public class WRCommands
{
	public static class ClearFrequencyCommand extends FillCommand
	{
		public static void register(CommandDispatcher<CommandSource> dispatcher)
		{
			LiteralArgumentBuilder<CommandSource> command = Commands.literal("wr")
					.requires((commandSource) -> commandSource.hasPermission(2))
					.then(Commands.literal("clear")
							.then(Commands.argument("frequency", IntegerArgumentType.integer(0, Short.toUnsignedInt(Short.MAX_VALUE)))
									.executes(ClearFrequencyCommand::clearFrequency))
							.then(Commands.literal("all")
									.executes(ClearFrequencyCommand::clearAll)));

			dispatcher.register(command);
		}

		private static int clearFrequency(CommandContext<CommandSource> context)
		{
			CommandSource source = context.getSource();
			ServerWorld world = source.getLevel();
			RedstoneNetwork network = RedstoneNetwork.get(world);
			short frequency = (short) IntegerArgumentType.getInteger(context, "frequency");

			if (!network.isChannelActive(frequency))
			{
				source.sendFailure(new TranslationTextComponent(LangKeys.COMMAND_CLEAR_FAILURE));
				return 0;
			}

			network.clearFrequency(frequency);
			source.sendSuccess(new TranslationTextComponent(LangKeys.COMMAND_CLEAR_SUCCESS).append(" " + frequency), true);
			return Command.SINGLE_SUCCESS;
		}

		private static int clearAll(CommandContext<CommandSource> context)
		{
			CommandSource source = context.getSource();
			ServerWorld world = source.getLevel();
			RedstoneNetwork.get(world).clearAll();
			source.sendSuccess(new TranslationTextComponent(LangKeys.COMMAND_CLEAR_SUCCESS_ALL), true);
			return Command.SINGLE_SUCCESS;
		}
	}

	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event)
	{
		ClearFrequencyCommand.register(event.getDispatcher());
	}
}
