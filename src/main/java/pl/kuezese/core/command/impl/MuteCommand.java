package pl.kuezese.core.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;

public class MuteCommand extends Command {

	public MuteCommand() {
		super("mute", "cm.mute");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie: &a/mute <nick> [powod]"));
			return true;
		}

		Player other = this.core.getServer().getPlayerExact(args[0]);

		if (other == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
			return true;
		}

		User user = this.userManager.get(other.getName());

		if (user.getMuteTime() != 0L) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest juz &awyciszony&7."));
			return true;
		}


		String reason = args.length < 2 ? "Brak" : StringUtils.join(args, " ", 1, args.length);

		user.setMuteTime(-1);

		this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &7" + other.getName() + " &7zostal permanentnie wyciszony na tym serwerze za &a" + reason + " &7przez &a" + sender.getName() + "&7."));

		return true;
	}
}
