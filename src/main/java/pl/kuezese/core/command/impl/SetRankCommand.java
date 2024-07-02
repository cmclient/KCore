package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class SetRankCommand extends Command {

    public SetRankCommand() {
        super("setrank", "root.setrank");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &7/setrank <nick> <ranga>"));
            return true;
        }

        this.core.getServer().dispatchCommand(this.core.getServer().getConsoleSender(), "lp user " + args[0] + " group set " + args[1]);
        sender.sendMessage(ChatHelper.color(" &8>> &7Nadano range &a" + args[1] + " &7dla gracza &a" + args[0] + "&7."));
		return true;
	}
}