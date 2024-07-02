package pl.kuezese.core.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class KickCommand extends Command {

	public KickCommand() {
		super("kick", "cm.kick");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/kick <gracz> [powod]"));
			return true;
		}

		Player other = this.core.getServer().getPlayer(args[0]);

		if (other == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
			return true;
		}

		String reason = args.length < 2 ? "Brak" : StringUtils.join(args, " ", 1, args.length);

		this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &a" + other.getName() + " &7zostal wyrzucony z serwera za &a" + reason + " &7przez &a" + sender.getName() + "&7."));

		other.kickPlayer(ChatHelper.color("&8>> &7Zostales wyrzucony\n&8>> &7Przez admina: &c" + sender.getName() + "\n&8>> &7Powod: &c" + reason));
		return true;
	}
}
