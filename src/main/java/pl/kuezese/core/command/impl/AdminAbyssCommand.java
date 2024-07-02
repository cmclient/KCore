package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class AdminAbyssCommand extends Command {

    public AdminAbyssCommand() {
        super("adminabyss", "cm.adminabyss");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        this.core.getAbyss().time = 6;
        this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Administrator &a" + sender.getName() + " &7wymusil otwarcie &aotchlani&7."));
        return true;
    }
}
