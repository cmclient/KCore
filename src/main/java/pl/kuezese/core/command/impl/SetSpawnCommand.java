package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class SetSpawnCommand extends Command {

    public SetSpawnCommand() {
        super("setspawn", "root.setspawn");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        Player p = sender instanceof Player ? ((Player) sender) : null;

        if (p == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        p.getWorld().setSpawnLocation(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());

        core.getConfiguration().setSpawnLoc(p.getLocation());
        core.getConfiguration().save(this.core);

        sender.sendMessage(ChatHelper.color(" &8>> &7Ustawiono lokalizacje &aspawnu &7na: &a" + p.getLocation().getBlockX()
                + "&7, &a" + p.getLocation().getBlockY() + "&7, &a" + p.getLocation().getBlockZ() + "&7."));
		return true;
	}
}