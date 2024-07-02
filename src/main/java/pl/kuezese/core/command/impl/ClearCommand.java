package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class ClearCommand extends Command {

	public ClearCommand() {
		super("clear", "cm.clear", "ci");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (args.length == 0) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			ChatHelper.title(p, "&8• &2&lEKWIPUNEK &8•", "&7Twoj ekwipunek zostal &awyczyszczony", 10, 40, 10);
			return true;
		}

		Player other = this.core.getServer().getPlayer(args[0]);

		if (other == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &coffline&7."));
			return true;
		}

		other.getInventory().clear();
		other.getInventory().setArmorContents(null);

		ChatHelper.title(p, "&8• &2&lEKWIPUNEK &8•", "&7Ekwipunek gracza &a" + other.getName() + " &7zostal &awyczyszczony", 10, 40, 10);
		ChatHelper.title(other, "&8• &2&lEKWIPUNEK &8•", "&aAdmin &a" + sender.getName() + " &7wyczyscil Ci ekwipunek", 10, 40, 10);
		return true;
	}
}

