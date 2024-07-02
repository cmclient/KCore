package pl.kuezese.core.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class ReplyCommand extends Command {

    public ReplyCommand() {
        super("reply", "", "r");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/reply <wiadomosc>"));
            return true;
        }

        String name = this.tellManager.get(sender.getName());

        if (name == null || this.core.getServer().getPlayerExact(name) == null) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Nie masz komu &aodpisac&7."));
            return true;
        }

        Player other = this.core.getServer().getPlayerExact(name);

        String message = StringUtils.join(args, " ");
        sender.sendMessage(ChatHelper.color(" &8>> &eJa &7-> &e" + other.getName() + "&7: &e" + message));
        other.sendMessage(ChatHelper.color(" &8>> &e" + sender.getName() + " &7-> &eJa&7: &e" + message));

        this.tellManager.add(sender.getName(), other.getName());
        return true;
    }
}
