package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class TpDenyCommand extends Command {

	public TpDenyCommand() {
		super("tpdeny", "");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		String name = this.teleportManager.get(sender.getName());

		if (name == null || this.core.getServer().getPlayerExact(name) == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Nikt nie chce sie do Ciebie &aprzeteleportowac&7."));
			return true;
		}

		Player other = this.core.getServer().getPlayerExact(name);

		p.sendMessage(ChatHelper.color(" &8>> &7Odrzuciles prosbe o teleportacje."));
		other.sendMessage(ChatHelper.color(" &8>> &7Gracz &a" + p.getName() + " &7odrzucil prosbe o teleportacje."));

		this.teleportManager.remove(other.getName());
		return true;
	}
}
