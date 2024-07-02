package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class GammaCommand extends Command {

	public GammaCommand() {
		super("gamma", "", "fullbright");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}


		if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
			ChatHelper.title(p, "&8• &2&lGAMMA &8•", "&7Zmieniono tryb &aFull Bright &7na &cWYLACZONY", 10,40, 10);
			p.removePotionEffect(PotionEffectType.NIGHT_VISION);
			return true;
		}

		ChatHelper.title(p, "&8• &2&lGAMMA &8•", "&7Zmieniono tryb &aFull Bright &7na &aWLACZONY", 10,40, 10);
		p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
		return true;
	}
}
