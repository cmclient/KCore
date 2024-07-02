package pl.kuezese.core.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class MsgCommand extends Command {

    public MsgCommand() {
        super("msg", "", "m", "tell");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/msg <gracz> <wiadomosc>"));
            return true;
        }

        Player other = this.core.getServer().getPlayer(args[0]);

        if (other == null) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Podany gracz jest &aoffline&7."));
            return true;
        }

//        if (this.core.getChatManager().isRequireLvl() && !sender.hasPermission("cm.noslowmode")) {
//            User user = this.userManager.get(sender.getName());
//            if (user.getLvl() < 2) {
//                sender.sendMessage(ChatHelper.color("&8&m---------------------------------------"));
//                sender.sendMessage(ChatHelper.color(" &8>> &7&oMusisz posiadac 2 poziom kopania aby moc pisac."));
//                sender.sendMessage("");
//                sender.sendMessage(ChatHelper.color(" &8• &7&oDbamy, aby czat pozbawiony byl &cniechcianych wiadomosci&7."));
//                sender.sendMessage(ChatHelper.color(" &8• &7&oTwoj aktualny poziom kopania: &c" + user.getLvl()));
//                sender.sendMessage(ChatHelper.color("&8&m---------------------------------------"));
//                return true;
//            }
//        }

        String message = StringUtils.join(args, " ", 1, args.length);
        sender.sendMessage(ChatHelper.color(" &8>> &eJa &7-> &e" + other.getName() + "&7: &e" + message));
        other.sendMessage(ChatHelper.color(" &8>> &e" + sender.getName() + " &7-> &eJa&7: &e" + message));

        this.tellManager.add(sender.getName(), other.getName());
        return true;
    }
}
