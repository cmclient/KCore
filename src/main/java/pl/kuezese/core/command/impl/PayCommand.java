package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;

public class PayCommand extends Command {

	public PayCommand() {
		super("pay", "");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (args.length < 2) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/pay <gracz> <ilosc>"));
			return true;
		}

		User user = this.userManager.get(sender.getName());

		Player other = this.core.getServer().getPlayerExact(args[0]);

		if (other == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
			return true;
		}

		if (other == sender) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Nie mozesz wysylac coins do siebie."));
			return true;
		}

		Float coins;

		try {
			coins = Float.parseFloat(args[1]);
		} catch (NumberFormatException ex) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podana liczba jest &anieprawidlowa&7."));
			return true;
		}

		if (coins.isInfinite() || coins.isNaN()) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podana liczba jest &anieprawidlowa&7."));
			return true;
		}

		if (user.getCoins() < coins) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Nie posiadasz tylu coins."));
			return true;
		}

		if (coins < 1) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Nie mozesz wysylac coins na minusie."));
			return true;
		}

		User user1 = this.userManager.get(other.getName());

		user.removeCoins(coins);
		user1.addCoins(coins);

		sender.sendMessage(ChatHelper.color(" &8>> &7Wyslales &a" + coins + "$ &7do gracza &a" + user1.getName()));
		other.sendMessage(ChatHelper.color(" &8>> &7Otrzymales &a" + coins + "$ &7od gracza &a" + sender.getName()));
		return true;
	}
}
