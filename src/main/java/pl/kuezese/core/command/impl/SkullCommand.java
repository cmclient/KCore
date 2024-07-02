package pl.kuezese.core.command.impl;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class SkullCommand extends Command {

	public SkullCommand() {
		super("skull", "cm.skull", "head");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player player = sender instanceof Player ? ((Player) sender) : null;

		if (player == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie: &a/skull <nick>"));
			return true;
		}

		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		meta.setOwner(args[0]);
		head.setItemMeta(meta);

		player.getInventory().addItem(head);
		sender.sendMessage(ChatHelper.color(" &8>> &7Otrzymales glowe gracza: &a" + args[0]));
		return true;
	}
}
