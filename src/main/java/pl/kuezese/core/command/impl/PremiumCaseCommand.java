package pl.kuezese.core.command.impl;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.ItemMaker;

public class PremiumCaseCommand extends Command {

	public PremiumCaseCommand() {
		super("premiumcase", "cm.premiumcase", "pcase");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		if (args.length < 2) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/premiumcase <all/gracz> <ilosc>."));
			return true;
		}

		int amount;

		try {
			amount = Integer.parseInt(args[1]);
		} catch (NumberFormatException ex) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Podana liczba jest &anieprawidlowa&7."));
			return true;
		}

		if (args[0].equalsIgnoreCase("all")) {
			ItemMaker builder = new ItemMaker(Material.CHEST, amount).setName(ChatHelper.color("&8[&9&lPremiumCase&8]"));
			this.core.getServer().getOnlinePlayers().forEach(p -> p.getInventory().addItem(builder.make()).values().forEach(is -> p.getWorld().dropItem(p.getLocation(), is)));
			this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Administrator &a" + sender.getName() + " &7rozdal wszystkim &aPremiumCase x" + args[1] + "&7."));
			return true;
		}

        Player other = this.core.getServer().getPlayerExact(args[0]);
        if (other == null) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
            return true;
        }
        ItemMaker builder = new ItemMaker(Material.CHEST, amount).setName(ChatHelper.color("&8[&9&lPremiumCase&8]"));
        other.getInventory().addItem(builder.make());
        sender.sendMessage(ChatHelper.color(" &8>> &7Dales &9Premium Case x" + args[1] + " &7graczowi &a" + other.getName()));
        other.sendMessage(ChatHelper.color(" &8>> &7Otrzymales &9Premium Case x" + args[1] + " &7od Administratora &a" + sender.getName()));
        return true;
	}
}
