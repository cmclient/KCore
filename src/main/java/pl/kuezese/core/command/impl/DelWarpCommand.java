package pl.kuezese.core.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.manager.impl.WarpManager;

public class DelWarpCommand extends Command {

    public DelWarpCommand() {
        super("delwarp", "root.delwarp");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/delwarp <nazwa>"));
            return true;
        }

        Player p = sender instanceof Player ? ((Player) sender) : null;

        if (p == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        String name = StringUtils.join(args, " ", 0, args.length);
        WarpManager.Warp warp = this.core.getWarpManager().get(name);

        if (warp == null) {
            sender.sendMessage(ChatHelper.color(" &8>> &cPodany warp nie istnieje."));
            return true;
        } else {
            this.core.getWarpManager().getWarps().remove(warp);
        }

        this.core.getWarpManager().save();
        sender.sendMessage(ChatHelper.color(" &8>> &7Usunieto warp: &a" + warp.getName()));
		return true;
	}
}