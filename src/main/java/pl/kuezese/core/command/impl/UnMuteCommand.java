package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;

public class UnMuteCommand extends Command {

	public UnMuteCommand() {
		super("unmute", "cm.unmute");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie: &c/unmute <nick>"));
			return true;
		}

		Player other = this.core.getServer().getPlayerExact(args[0]);

		if (other == null) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
			return true;
		}

		User user = this.userManager.get(other.getName());

		if (user.getMuteTime() == 0L) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz nie jest &awyciszony&7."));
			return true;
		}

		user.setMuteTime(0L);

		this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &a" + user.getName() + " &7zostal odciszony na tym serwerze przez &a" + sender.getName() + "&7."));
		return true;
	}
}
