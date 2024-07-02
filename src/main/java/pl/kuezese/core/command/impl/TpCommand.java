package pl.kuezese.core.command.impl;

import io.papermc.lib.PaperLib;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class TpCommand extends Command {

	public TpCommand() {
		super("teleport", "cm.tp", "tp");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/tp <gracz>"));
			return true;
		}

		Player other = this.core.getServer().getPlayer(args[0]);

		if (other == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
			return true;
		}

		PaperLib.teleportAsync(p, other.getLocation())
				.thenAccept(result -> ChatHelper.title(p, "&8• &2&lTELEPORTACJA&8•", "&7Przeteleportowano do gracza &a" + other.getName(), 10, 40, 10));
		return true;
	}
}
