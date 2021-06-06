package rzk.wirelessredstone.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import rzk.wirelessredstone.rsnetwork.RedstoneNetwork;

public class WRCommand extends CommandBase
{
	@Override
	public String getName()
	{
		return "wr";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return LangKeys.COMMAND_CLEAR;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args == null || args.length != 2 || !args[0].equals("clear") || (!args[1].equals("all") && !args[1].matches("[0-9]+")))
			throw new WrongUsageException(I18n.format(getUsage(sender)));

		World world = server.getEntityWorld();
		if (!world.isRemote)
		{
			RedstoneNetwork network = RedstoneNetwork.get(world, false);

			if (network != null)
			{
				if (args[1].equals("all"))
				{
					network.clearAll();
					notifyCommandListener(sender, this, LangKeys.COMMAND_CLEAR_SUCCESS_ALL);
				}
				else
				{
					short frequency = Short.parseShort(args[1]);
					network.clearFrequency(frequency);
					notifyCommandListener(sender, this, LangKeys.COMMAND_CLEAR_SUCCESS, " " + frequency);
				}
			}
		}
	}
}
