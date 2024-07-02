package pl.kuezese.core.command.impl;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

import java.util.Arrays;


public class RepairCommand extends Command {

    public RepairCommand() {
        super("repair", "");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        Player p = sender instanceof Player ? ((Player) sender) : null;

        if (p == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        if (args.length != 0 && args[0].equalsIgnoreCase("all")) {
            if (!sender.hasPermission("cm.repair.all")) {
                sender.sendMessage(ChatHelper.color(" &8>> &cNie posiadasz uprawnien do tej komendy. &8(&7cm.repair.all&8)"));
                return true;
            }
            Arrays.stream(p.getInventory().getContents()).filter(is -> is != null && !is.getType().isBlock() && is.getType().getMaxDurability() >= 1 && is.getType() != Material.AIR && is.getType() != Material.GOLDEN_APPLE && is.getType() != Material.POTION && !is.getType().isBlock()).forEach(itemStack -> itemStack.setDurability((short) 0));
            Arrays.stream(p.getInventory().getArmorContents()).filter(is -> is != null && !is.getType().isBlock() && is.getType().getMaxDurability() >= 1 && is.getType() != Material.AIR && is.getType() != Material.GOLDEN_APPLE && is.getType() != Material.POTION && !is.getType().isBlock()).forEach(is -> is.setDurability((short) 0));
            p.updateInventory();
            sender.sendMessage(ChatHelper.color(" &8>> &aNaprawiles wszystkie przedmioty w ekwipunku."));
            return true;
        }


        ItemStack is = p.getItemInHand();

        if (is.getType().isBlock() || is.getType().getMaxDurability() < 1 || is.getType() == Material.AIR || is.getType() == Material.GOLDEN_APPLE || is.getType() == Material.POTION || is.getType().isBlock()) {
            sender.sendMessage(ChatHelper.color(" &8>> &cTen przedmiot nie moze zostac naprawiony."));
            return true;
        }

        is.setDurability((short) 0);
        p.updateInventory();
        sender.sendMessage(ChatHelper.color(" &8>> &7Naprawiles przedmiot: &a" + is.getType().name()));
        return true;
    }
}
