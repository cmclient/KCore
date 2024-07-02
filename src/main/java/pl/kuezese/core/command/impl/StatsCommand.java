package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.menu.ProfieMenu;

public class StatsCommand extends Command {

    public StatsCommand() {
        super("stats", "", "stat", "staty", "statystyki", "profil", "profile");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        Player p = sender instanceof Player ? ((Player) sender) : null;

        if (p == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        if (args.length != 0) {
            Player other = this.core.getServer().getPlayer(args[0]);
            new ProfieMenu(core, other).open(p);
            return true;
        }

        new ProfieMenu(core, p).open(p);
        return true;
    }
}
