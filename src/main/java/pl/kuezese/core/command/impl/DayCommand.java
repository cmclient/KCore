package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class DayCommand extends Command {

	public DayCommand() {
		super("day", "cm.time");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		p.getWorld().setTime(1500);

		this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Na serwerze czas zostal ustawiony na &adzien&7."));
		return true;
	}
}