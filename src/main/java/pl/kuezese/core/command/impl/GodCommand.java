package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;

public class GodCommand extends Command {

	public GodCommand() {
		super("god", "cm.god");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (args.length == 0) {
			User user = this.userManager.get(p.getName());
			user.setGodmode(!user.isGodmode());
			ChatHelper.title(p, "&8• &2&lGOD &8•", "&7Zmieniono tryb &aniesmiertelnosci &7na &" + (user.isGodmode() ? "aWLACZONY" : "cWYLACZONY"), 10,40, 10);
			return true;
		}

		Player other = this.core.getServer().getPlayer(args[0]);

		if (other == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
			return true;
		}

		if (!other.hasPermission("cm.god")) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &anie posiada uprawnien&7."));
			return true;
		}


		User user = this.userManager.get(other.getName());
		user.setGodmode(!user.isGodmode());
		ChatHelper.title(p, "&8• &2&lGOD &8•", "&7Zmieniono tryb &aniesmiertelnosci &7gracza &a" + other.getName() + " &7na &" + (user.isGodmode() ? "aWLACZONY" : "cWYLACZONY"), 10,40, 10);
		ChatHelper.title(other, "&8• &2&lGOD &8•", "&7Zmieniono tryb &aniesmiertelnosci &7na &" + (user.isGodmode() ? "aWLACZONY" : "cWYLACZONY") + " &7przez &a" + p.getName(), 10,40, 10);
		return true;
	}
}

