package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.object.User;
import pl.kuezese.core.helper.ChatHelper;

public class DelHomeCommand extends Command {

    public DelHomeCommand() {
        super("delhome", "");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        Player p = sender instanceof Player ? ((Player) sender) : null;

        if (p == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        User user = this.userManager.get(sender.getName());

        if (user.getHome() == null) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Nie posiadasz ustawionego &adomu&7."));
            return true;
        }

        user.setHome(null);
        sender.sendMessage(ChatHelper.color(" &8>> &7Twoj dom zostal &ausuniety&7."));
        return true;
    }
}
