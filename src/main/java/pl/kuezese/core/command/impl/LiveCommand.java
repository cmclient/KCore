package pl.kuezese.core.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class LiveCommand extends Command {

	public LiveCommand() {
		super("live", "cm.live");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/live <link>"));
			return true;
		}

		if (args[0].startsWith("www") || args[0].startsWith("youtube") || args[0].startsWith("http") || args[0].startsWith("twitch")) {
			this.core.getServer().broadcastMessage("");
			this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &a" + sender.getName() + " &7prowadzi live."));
			this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Link: &a" + StringUtils.join(args, " ")));
			this.core.getServer().broadcastMessage("");
			return true;
		}
		sender.sendMessage(ChatHelper.color(" &8>> &7Podales &anieprawidlowy &7link."));
		return true;
	}
}
