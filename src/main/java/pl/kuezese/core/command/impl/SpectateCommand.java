package pl.kuezese.core.command.impl;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class SpectateCommand extends Command {

	public SpectateCommand() {
		super("spectate", "cm.spectate", "spec");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/spectate <gracz>"));
			return true;
		}

		Player other = this.core.getServer().getPlayer(args[0]);

		if (other == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
			return true;
		}

		if (p == other) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Nie mozesz obserwowac siebie samego."));
			return true;
		}

		p.setGameMode(GameMode.SPECTATOR);
		p.setSpectatorTarget(other);
		sender.sendMessage(ChatHelper.color(" &8>> &7Obserwujesz gracza &a" + other.getName()));
		return true;
	}
}
