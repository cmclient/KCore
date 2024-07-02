package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class AlertsCommand extends Command {

	public AlertsCommand() {
		super("alerts", "", "powiadomienia");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player player = sender instanceof Player ? ((Player) sender) : null;

		if (player == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		this.core.getServer().dispatchCommand(this.core.getServer().getConsoleSender(), "lp user " + sender.getName() + " permission set i.case " + !sender.hasPermission("i.case"));
		sender.sendMessage(ChatHelper.color(" &8>> &7Powiadomienia zostaly: &" + (sender.hasPermission("i.case") ? "cwylaczone" : "awlaczone")));
		return true;
	}
}
