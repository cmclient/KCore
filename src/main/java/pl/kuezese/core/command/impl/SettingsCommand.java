package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.menu.SettingsMenu;

public class SettingsCommand extends Command {

	public SettingsCommand() {
		super("settings", "cm.panel", "ustawienia", "panel");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		new SettingsMenu(this.core).open(p);
		return true;
	}
}
