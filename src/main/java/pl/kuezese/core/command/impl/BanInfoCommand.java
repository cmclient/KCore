package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.DateHelper;
import pl.kuezese.core.manager.impl.BanManager;

public class BanInfoCommand extends Command {

	public BanInfoCommand() {
		super("baninfo", "cm.baninfo");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie: &a/baninfo <nick>"));
			return true;
		}

		BanManager.Ban ban = this.banManager.get(args[0]);

		if (ban == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Ten nick nie jest &azbanowany&7."));
			return true;
		}

		sender.sendMessage(ChatHelper.color(" &8>> &7Ban dla gracza: &a" + ban.getName()));
		sender.sendMessage(ChatHelper.color(" &8>> &7Administrator: &a" + ban.getAdmin()));
		sender.sendMessage(ChatHelper.color(" &8>> &7Czas: &a" + (ban.getTime() == 0L ? "Na zawsze" : DateHelper.formatDateDiff(ban.getTime()))));
		sender.sendMessage(ChatHelper.color(" &8>> &7Powod: &a" + ban.getReason()));

		return true;
	}
}
