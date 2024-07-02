package pl.kuezese.core.command.impl;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;

public class VanishCommand extends Command {

	public VanishCommand() {
		super("vanish", "cm.vanish", "v");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		User user = this.userManager.get(p.getName());
		user.setVanished(!user.isVanished());

		this.core.getServer().getOnlinePlayers().forEach(other -> {
			if (other.hasPermission("cm.vanish")) {
				ChatHelper.actionBar(other, "&8>> &7Administrator &a" + p.getName() + " &7jest teraz &a" + (user.isVanished() ? "niewidzialny" : "widzialny") + ".");
			} else if (other != p) {
				if (user.isVanished()) other.hidePlayer(p);
				else other.showPlayer(p);
			}
		});

		p.playSound(p.getLocation(), Sound.BAT_DEATH, 1.0F, 1.0F);
		ChatHelper.title(p, "&8• &2&lVANISH &8•", "&7Jestes teraz &a" + (user.isVanished() ? "niewidzialny" : "widzialny") + "&7.", 10, 40, 10);
		return true;
	}
}