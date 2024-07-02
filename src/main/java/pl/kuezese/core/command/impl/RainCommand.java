package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class RainCommand extends Command {

	public RainCommand() {
		super("rain", "cm.time");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		p.getWorld().setStorm(true);

	    this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Na serwerze zostal wlaczony &7deszcz&7."));
		return true;
	}
}
