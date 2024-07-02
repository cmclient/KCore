package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class SunCommand extends Command {

	public SunCommand() {
		super("sun", "cm.time");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		p.getWorld().setStorm(false);

	    this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Na serwerze zostal wylaczony &adeszcz&7."));
		return true;
	}
}