package pl.kuezese.core.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

import java.util.Map;
import java.util.WeakHashMap;

public class HelpOpCommand extends Command {

	public HelpOpCommand() {
		super("helpop", "");
	}

	private final Map<String, Long> times = new WeakHashMap<>();

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/helpop <wiadomosc>"));
			return true;
		}

		if (sender instanceof Player) {
			if (times.containsKey(sender.getName()) && times.get(sender.getName()) > System.currentTimeMillis()){
				long time = times.get(sender.getName());
				sender.sendMessage(ChatHelper.color(" &8>> &7Kolejna wiadomosc na &a/helpop &7bedziesz mogl wyslac za &a" + (time - System.currentTimeMillis()) / 1000 + "s&7."));
				return true;
			}

			times.put(sender.getName(), System.currentTimeMillis() + 5000L);
		}

		String message = StringUtils.join(args, " ");

		int admins = this.core.getServer().broadcast(ChatHelper.color("&8[&cHelpOP&8] &a" + sender.getName() + "&8: &f" + message), "cm.helpop");
		
		sender.sendMessage(ChatHelper.color(" &8>> &7Wiadomosc &a" + message + " &7zostala wyslana do &a" + admins + " administratorow&7."));
		return true;
	}
}
