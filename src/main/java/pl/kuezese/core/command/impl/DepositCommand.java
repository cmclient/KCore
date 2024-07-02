package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.menu.DepositMenu;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;

public class DepositCommand extends Command {

    public DepositCommand() {
        super("deposit", "", "depozyt", "schowek", "limit", "limity");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        Player p = sender instanceof Player ? ((Player) sender) : null;

        if (p == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        if (args.length != 0 && args[0].equalsIgnoreCase("all")) {
            User user = this.userManager.get(sender.getName());
            this.core.getLimitManager().withdrawAll(p, user);
            p.sendMessage(ChatHelper.color(" &8>> &7Wyplaciles wszystkie przedmioty z depozytu&7."));
            return true;
        }

        new DepositMenu(core).open(p);
        return true;
    }
}
