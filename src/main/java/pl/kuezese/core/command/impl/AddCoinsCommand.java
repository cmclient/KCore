package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;

public class AddCoinsCommand extends Command {

    public AddCoinsCommand() {
        super("addcoins", "root.addcoins");
    }

    @Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		if (args.length < 2){
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/addcoins <gracz> <ilosc>"));
			return true;
		}

		User user = this.userManager.get(args[0]);

		if (user == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz nigdy nie byl na &aserwerze&7."));
			return true;
		}

		float coins;

		try {
			coins = Float.parseFloat(args[1]);
		} catch (NumberFormatException ex) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podana liczba jest &anieprawidlowa&7."));
			return true;
		}

		user.addCoins(coins);
		user.update();
		sender.sendMessage(ChatHelper.color(" &8>> &7Dodano &a" + coins + "$ &7graczowi &a" + user.getName()));
		return true;
	}
}
