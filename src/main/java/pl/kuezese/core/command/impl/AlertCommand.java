package pl.kuezese.core.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

import java.util.Locale;

public class AlertCommand extends Command {

	public AlertCommand() {
		super("alert", "cm.alert", "bc", "broadcast");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/alert <chat/title/subtitle/actionbar/noprefix> <wiadomosc>"));
			return true;
		}

		String message = StringUtils.join(args, " ", 1, args.length);

		switch (args[0].toLowerCase(Locale.ROOT)) {
			case "chat": {
				this.core.getServer().broadcastMessage("");
				this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Ogloszenie: &a" + message));
				this.core.getServer().broadcastMessage("");
				break;
			}
			case "title": {
				this.core.getServer().getOnlinePlayers().forEach(p -> ChatHelper.title(p, message, "", 10, 40, 10));
				break;
			}
			case "subtitle": {
				this.core.getServer().getOnlinePlayers().forEach(p -> ChatHelper.title(p, "&8• &4Alert &8•", message, 10, 40, 10));
				break;
			}
			case "actionbar": {
				this.core.getServer().getOnlinePlayers().forEach(p -> ChatHelper.actionBar(p, "&8>> &7" + message));
				break;
			}
			case "noprefix": {
				this.core.getServer().broadcastMessage(ChatHelper.color(message));
				break;
			}
			default: {
				sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/alert <chat/title/subtitle/actionbar/noprefix> <wiadomosc>"));
			}
		}
		return true;
	}
}