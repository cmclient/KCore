package pl.kuezese.core.command.impl;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.manager.impl.WarpManager;

public class SetWarpCommand extends Command {

    public SetWarpCommand() {
        super("setwarp", "root.setwarp");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/setwarp <nazwa>"));
            return true;
        }

        Player p = sender instanceof Player ? ((Player) sender) : null;

        if (p == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        ItemStack is = p.getItemInHand();

        if (is.getType() == Material.AIR) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Musisz trzymac przedmiot w &arece&7."));
            return true;
        }

        String name = StringUtils.join(args, " ", 0, args.length);
        WarpManager.Warp warp = this.core.getWarpManager().get(name);

        if (warp == null) {
            warp = new WarpManager.Warp(name, p.getItemInHand().getType(), p.getLocation());
            this.core.getWarpManager().getWarps().add(warp);
        } else {
            warp.setLocation(p.getLocation());
        }

        this.core.getWarpManager().save();
        sender.sendMessage(ChatHelper.color(" &8>> &7Ustawiono lokalizacje warpu &a" + name + " &7na: &a" + p.getLocation().getBlockX()
                + "&7, &a" + p.getLocation().getBlockY() + "&7, &a" + p.getLocation().getBlockZ() + "&7."));
		return true;
	}
}