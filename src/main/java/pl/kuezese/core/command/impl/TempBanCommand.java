package pl.kuezese.core.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.DateHelper;
import pl.kuezese.core.manager.impl.BanManager;

public class TempBanCommand extends Command {

	public TempBanCommand() {
		super("tempban", "cm.tempban");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie: &a/tempban <nick> <czas> [powod]"));
			return true;
		}

		if (this.banManager.getBans().containsKey(args[0])) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest juz &azbanowany&7."));
			return true;
		}

		Player other = this.core.getServer().getPlayerExact(args[0]);
		long time = DateHelper.stringToTime(args[1]);
		String reason = args.length < 3 ? "Brak" : StringUtils.join(args, " ", 2, args.length);
		BanManager.Ban ban = new BanManager.Ban(args[0], sender.getName(), time, reason);
		this.banManager.getBans().put(args[0], ban);

		String dateDiff = DateHelper.formatDateDiff(time);
		this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &a" + args[0] + " &7zostal czasowo zbanowany na tym serwerze za &a" + reason + " &7przez &a" + sender.getName() + " &7na &a" + dateDiff + "&7."));

		if (other != null) {
			other.kickPlayer(ChatHelper.color("&8>> &7Zostales zbanowany\n&8>> &7Przez admina: &c" + sender.getName() + "\n&8>> &7Czas: &c" + dateDiff + "\n&8>> &7Powod: &c" + reason + "&7.\n\n&8>> &7Unbana mozesz zakupic na: &awww.coremax.pl"));
        }
		return true;
	}
}
