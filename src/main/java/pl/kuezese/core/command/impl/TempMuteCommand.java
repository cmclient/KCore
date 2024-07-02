package pl.kuezese.core.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.DateHelper;
import pl.kuezese.core.object.User;

public class TempMuteCommand extends Command {

	public TempMuteCommand() {
		super("tempmute", "cm.tempmute");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie: &a/tempmute <nick> <czas> [powod]"));
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

		long time = DateHelper.stringToTime(args[1]);
		String reason = args.length < 3 ? "Brak" : StringUtils.join(args, " ", 2, args.length);
		user.setMuteTime(time);

		this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &a" + args[0] + " &7zostal czasowo wyciszony na tym serwerze za &a" + reason + " &7przez &a" + sender.getName() + " &7na &a" + DateHelper.formatDateDiff(time) + "&7."));
		return true;
	}
}
