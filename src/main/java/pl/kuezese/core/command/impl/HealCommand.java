package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class HealCommand extends Command {

	public HealCommand() {
		super("heal", "cm.heal");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (args.length == 0) {
			p.setHealth(20);
			p.setFoodLevel(20);
			p.setFireTicks(0);
			p.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(p::removePotionEffect);
			ChatHelper.title(p, "&8• &2&lHEAL &8•", "&7Zostales &auleczony&7.", 10,40, 10);
			return true;
		}

		if (sender.hasPermission("cm.heal.admin")) {
			Player other = this.core.getServer().getPlayer(args[0]);
			if (other == null) {
				sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
				return true;
			}
			other.setHealth(20);
			other.setFoodLevel(20);
			other.setFireTicks(0);
			other.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(other::removePotionEffect);
			ChatHelper.title(p, "&8• &2&lHEAL &8•", "&7Uleczyles gracza &a" + other.getName() + "&7.", 10,40, 10);
			ChatHelper.title(other, "&8• &2&lHEAL &8•", "&7Zostales &auleczony &7przez &a" + p.getName() + "&7.", 10,40, 10);
		}
		return true;
	}
}
