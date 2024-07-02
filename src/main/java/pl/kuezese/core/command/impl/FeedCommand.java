package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class FeedCommand extends Command {

    public FeedCommand() {
        super("feed", "cm.feed");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        Player p = sender instanceof Player ? ((Player) sender) : null;

        if (p == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        p.setFoodLevel(20);

        ChatHelper.title(p, "&8• &2&lFEED &8•", "&7Twoj glod zostal &auzupelniony&7.", 10,40, 10);
        return true;
    }
}