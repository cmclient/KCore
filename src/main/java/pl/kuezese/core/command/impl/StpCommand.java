package pl.kuezese.core.command.impl;

import io.papermc.lib.PaperLib;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class StpCommand extends Command {

	public StpCommand() {
		super("stp", "cm.stp", "s", "tphere");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/stp <gracz>"));
			return true;
		}

		Player other = this.core.getServer().getPlayer(args[0]);

		if (other == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
			return true;
		}

		PaperLib.teleportAsync(other, p.getLocation()).thenAccept(result -> {
			ChatHelper.title(p, "&8• &2&lTELEPORTACJA &8•", "&7Przeteleportowano gracza &a" + other.getName() + " &7do siebie", 10,40, 10);
			ChatHelper.title(other, "&8• &2&lTELEPORTACJA &8•", "&7Administrator &a" + p.getName() + " &7przeteleportowal Cie do siebie", 10,40, 10);
		});
		return true;
	}
}
