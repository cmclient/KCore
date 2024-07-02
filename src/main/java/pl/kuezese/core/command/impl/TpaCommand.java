package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class TpaCommand extends Command {

	public TpaCommand() {
		super("tpa", "");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/tpa <gracz>"));
			return true;
		}

		Player other = this.core.getServer().getPlayer(args[0]);

		if (other == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
			return true;
		}

		sender.sendMessage(ChatHelper.color(" &8>> &7Wyslano prosbe o teleporatcje."));
		other.sendMessage(ChatHelper.color(" &8>> &7Gracz &a" + p.getName() + " &7chce sie do ciebie przeteleportowac."));
		other.sendMessage(ChatHelper.color(" &8>> &7Uzyj: &a/tpaccept &7aby zaakceptowac prosbe"));
		other.sendMessage(ChatHelper.color(" &8>> &7Lub: &a/tpdeny &7aby odrzucic prosbe"));

		this.teleportManager.add(p.getName(), other.getName());
		return true;
	}
}
