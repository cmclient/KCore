package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.StringHelper;
import pl.kuezese.core.task.impl.AbyssTask;

public class AbyssCommand extends Command {

    public AbyssCommand() {
        super("abyss", "", "otchlan");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        Player p = sender instanceof Player ? ((Player) sender) : null;

        if (p == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        AbyssTask abyssTask = this.core.getAbyss();

        if (!abyssTask.open) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Otchlan jest aktualnie &azamknieta&7."));
            sender.sendMessage(ChatHelper.color(" &8>> &7Otchlan zostanie otwarta za &a" + StringHelper.parseSeconds(abyssTask.time) + "&7."));
            return true;
        }

        if (abyssTask.inventories.isEmpty()) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Otchlan jest &apusta&7."));
            return true;
        }

        abyssTask.opened.put(p, 0);
        p.openInventory(abyssTask.inventories.get(0));
        return true;
    }
}
