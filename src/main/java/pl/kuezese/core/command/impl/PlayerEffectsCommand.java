package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.menu.effects.EffectsPlayerMenu;

public class PlayerEffectsCommand extends Command {

	public PlayerEffectsCommand() {
		super("playereffect", "", "eg", "efektgracz");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		new EffectsPlayerMenu(core).open(p);
		return true;
	}
}
