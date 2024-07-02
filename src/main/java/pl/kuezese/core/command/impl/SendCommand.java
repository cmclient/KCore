package pl.kuezese.core.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class SendCommand extends Command {

    public SendCommand() {
        super("send", "root.send");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/send <nick> <text>"));
            return true;
        }

        Player other = this.core.getServer().getPlayer(args[0]);

        if (other == null) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
            return true;
        }

        String message = StringUtils.join(args, " ", 1, args.length);
        other.chat(message);
        return true;
    }
}
