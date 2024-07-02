package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class KoszCommand extends Command {

	public KoszCommand() {
		super("kosz", "", "trash");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		Inventory inv = this.core.getServer().createInventory(p, 45, ChatHelper.color("&8>> &aKosz &8<<"));
		p.openInventory(inv);
		return true;
	}
}
