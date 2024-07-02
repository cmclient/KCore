package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.object.User;
import pl.kuezese.core.helper.ChatHelper;

public class CoinsCommand extends Command {

	public CoinsCommand() {
		super("coins", "", "monety");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player player = sender instanceof Player ? ((Player) sender) : null;

		if (player == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (args.length == 0) {
			User user = this.userManager.get(sender.getName());
			sender.sendMessage(ChatHelper.color(" &8>> &7Twoj portfel: &a" + user.getCoinsFormatted() + "$"));
			return true;
		}

		User user = this.userManager.get(args[0]);

		if (user == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
			return true;
		}

		sender.sendMessage(ChatHelper.color(" &8>> &7Portfel gracza &a" + user.getName() + "&7: &a" + user.getCoinsFormatted() + "$"));
		return true;
	}
}
