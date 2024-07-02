package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.UserHelper;
import pl.kuezese.core.object.User;

public class SellAllComand extends Command {

    public SellAllComand() {
        super("sellall", "");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        Player p = sender instanceof Player ? ((Player) sender) : null;

        if (p == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        User user = this.userManager.get(sender.getName());
        UserHelper.sellAll(this.core, p, user);
        return true;
    }
}
