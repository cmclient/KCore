package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.region.helper.StringHelper;

public class CoreCommand extends Command {

    public CoreCommand() {
        super("core", "cm.core", "tools");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Plugin: &a" + this.core.getDescription().getFullName()));
            sender.sendMessage(ChatHelper.color(" &8>> &a" + this.core.getDescription().getDescription()));
            sender.sendMessage(ChatHelper.color(" &8>> &7Autorzy: &a" + StringHelper.join(this.core.getDescription().getAuthors(), ", ")));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            long start = System.currentTimeMillis();
            this.core.getConfiguration().reload();
            this.core.getDropManager().reload();
            this.core.getWarpManager().reload();
            this.core.getShopManager().reload();
            this.core.getCaseManager().reload();
            this.core.getPandoraManager().reload();
            this.core.getCobblexManager().reload();
            this.core.getKitManager().reload();
            this.core.getVillagerManager().reload();
            this.core.getLimitManager().reload();
            long end = System.currentTimeMillis();
            sender.sendMessage(ChatHelper.color(" &8>> &7Plugin &a" + this.core.getDescription().getFullName() + " &7zostal przeladowany w " + (end - start) + "ms."));
        }
        return true;
    }
}
