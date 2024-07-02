package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;

public class SetHomeCommand extends Command {

    public SetHomeCommand() {
        super("sethome", "");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        Player p = sender instanceof Player ? ((Player) sender) : null;

        if (p == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        User user = this.userManager.get(p.getName());
        user.setHome(p.getLocation());
        p.sendMessage(ChatHelper.color(" &8>> &7Ustawiono nowa lokalizacje &adomu&7."));
        return true;
    }
}
