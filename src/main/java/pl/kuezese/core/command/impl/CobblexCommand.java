package pl.kuezese.core.command.impl;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.ItemMaker;

public class CobblexCommand extends Command {

	public CobblexCommand() {
		super("cobblex", "", "cx");
	}

	@Override
	public boolean onCommand(CommandSender sender, String s, String[] args) {
		Player p = sender instanceof Player ? ((Player) sender) : null;

		if (p == null) {
			sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
			return true;
		}

		if (!p.getInventory().containsAtLeast(new ItemStack(Material.COBBLESTONE), 576)) {
			sender.sendMessage(ChatHelper.color(" &8>> &7Musisz posiadac &c9 stakow cobblestone &7aby stworzyc &3CobbleX&7."));
			return true;
		}

		p.getInventory().removeItem(new ItemStack(Material.COBBLESTONE, 576));

		p.getInventory().addItem(new ItemMaker(Material.MOSSY_COBBLESTONE).setName(ChatHelper.color("&8>> &3CobbleX")).make());
		sender.sendMessage(ChatHelper.color(" &8>> &7Stworzyles &3CobbleX&7."));

		return true;
	}
}
