package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class EnderChestCommand extends Command {

	public EnderChestCommand() {
		super("enderchest", "cm.enderchest", "ec");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (args.length == 0) {
			p.openInventory(p.getEnderChest());
        	return true;
		}

        if (sender.hasPermission("cm.ec.admin")) {
			Player other = this.core.getServer().getPlayer(args[0]);
			if (other == null) {
				sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
				return true;
			}
        	p.openInventory(other.getEnderChest());
        }
		return true;
	}
}