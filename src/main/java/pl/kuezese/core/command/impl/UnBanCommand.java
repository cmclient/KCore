package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.manager.impl.BanManager;

public class UnBanCommand extends Command {

	public UnBanCommand() {
		super("unban", "cm.unban");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie: &a/unban <nick>"));
			return true;
		}

		if (sender.hasPermission("root.unban.all") && args[0].equalsIgnoreCase("all")) {
			int bans = this.banManager.getBans().size();

			this.core.getSql().update("DELETE FROM `bans`");
			this.banManager.getBans().clear();

			sender.sendMessage(ChatHelper.color(" &8>> &7Odbanowano &a" + bans + " graczy&7."));
			return true;
		}

		BanManager.Ban ban = this.banManager.get(args[0]);

		if (ban == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Ten nick nie jest &azbanowany&7."));
			return true;
		}

		this.banManager.getBans().remove(ban.getName());
		this.core.getSql().update("DELETE FROM `bans` WHERE `name`='" + ban.getName() + "'");

		this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &a" + ban.getName() + " &7zostal odbanowany na tym serwerze przez &a" + sender.getName() + "&7."));
		return true;
	}
}
