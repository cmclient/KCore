package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class ListCommand extends Command {

	public ListCommand() {
		super("list", "", "online");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		sender.sendMessage(ChatHelper.color(" &8>> &7Na serwerze jest aktualnie &a" + this.core.getServer().getOnlinePlayers().size() + "&7/&a" + this.core.getServer().getMaxPlayers() + " &7graczy."));
		return true;
	}
}
