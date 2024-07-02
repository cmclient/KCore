package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class YtPlusCommand extends Command {

	public YtPlusCommand() {
		super("yt+", "", "youtuber+");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		sender.sendMessage(ChatHelper.color("&8&m------------&8 ( &4Y&fT&4+ &8) &8&m------------&8 "));
		sender.sendMessage(ChatHelper.color(" &8>> &aPosiada to samo co ranga &b&lSPONSOR&7."));
		sender.sendMessage(ChatHelper.color(" &8>> &aSpecjalny kit YT: &7/kit&7."));
		sender.sendMessage(ChatHelper.color(" &8>> &a/live &8- &7Ogloszenie o live."));
		sender.sendMessage("");
		sender.sendMessage(ChatHelper.color(" &8>> &7Wymagania:"));
		sender.sendMessage(ChatHelper.color(" &8>> &a100 subskrybcji&7."));
		sender.sendMessage(ChatHelper.color(" &8>> &aPoprostu wrzucenie reklamy naszego serwera :)&7."));
		sender.sendMessage("");
		sender.sendMessage(ChatHelper.color(" &8>> &aPo odbior rangi stworz ticket na serwerze Discord &7dc.coremax.pl&7."));
		sender.sendMessage(ChatHelper.color("&8&m------------&8 ( &4Y&fT&4+ &8) &8&m------------&8 "));
		return true;
	}
}
