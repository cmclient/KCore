package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class DiscordCommand extends Command {

	public DiscordCommand() {
		super("discord", "", "dsc", "dc");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		sender.sendMessage(ChatHelper.color(" &8>> &7Nasz serwer Discord &9dc.coremax.pl"));
		return true;
	}
}
