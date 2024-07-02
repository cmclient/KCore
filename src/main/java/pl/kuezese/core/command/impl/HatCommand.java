package pl.kuezese.core.command.impl;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class HatCommand extends Command {

	public HatCommand() {
		super("hat", "cm.hat");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		ItemStack is = p.getItemInHand();

		if (is.getType() == Material.AIR || (!is.getType().isBlock() && is.getType() != Material.BANNER)) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Musisz trzymac przedmiot w &arece&7."));
			return true;
		}

		ItemStack[] contents = p.getInventory().getArmorContents();

		p.setItemInHand(contents[3] == null ? null : contents[3]);

		contents[3] = is;
		p.getInventory().setArmorContents(contents);

		sender.sendMessage(ChatHelper.color(" &8>> &7Zalozyles na glowe przedmiot &a" + is.getType().name() + "&7."));

		return true;
	}
}
