package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;

public class NearCommand extends Command {

    public NearCommand() {
        super("near","cm.near");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        Player p = sender instanceof Player ? ((Player) sender) : null;

        if (p == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        sender.sendMessage(ChatHelper.color(" &8>> &7Gracze blisko ciebie:"));

        p.getLocation().getWorld().getPlayers()
                .stream()
                .filter(other -> p.getLocation().distance(other.getLocation()) <= 100.0D)
                .filter(p::canSee)
                .map(other -> new AbstractMap.SimpleEntry<>(other.getName(), p.getLocation().distance(other.getLocation())))
                .sorted(Comparator.comparingDouble(
                        Map.Entry::getValue)) // Sort by distance
                .map(entry -> ChatHelper.color(" &8>> &a" + entry.getKey() + " &7(&a" + Math.floor(entry.getValue()) + "m&7)"))
                .forEach(sender::sendMessage);
        return true;
    }
}
