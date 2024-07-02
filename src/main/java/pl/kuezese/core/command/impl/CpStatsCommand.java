package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;
import pl.kuezese.region.helper.StringHelper;

import java.util.List;
import java.util.stream.Collectors;

public class CpStatsCommand extends Command {

    public CpStatsCommand() {
        super("cpstats", "cm.cmpack");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        sender.sendMessage(ChatHelper.color(" &8>> &7Statystyki CMClient"));
        List<String> verified = this.core.getServer().getOnlinePlayers().stream().map(player -> this.userManager.get(player.getName())).filter(User::isUsingClient).map(User::getName).collect(Collectors.toList());
        List<String> unverified = this.core.getServer().getOnlinePlayers().stream().map(player -> this.userManager.get(player.getName()).getName()).collect(Collectors.toList());
        unverified.removeAll(verified);

        sender.sendMessage(ChatHelper.color(" &8>> &7Gracze korzystajacy (&a" + verified.size() + "&7/&a" + this.core.getServer().getOnlinePlayers().size() + "&7): &a" + StringHelper.join(verified, ", ")));
        sender.sendMessage(ChatHelper.color(" &8>> &7Gracze nie korzystajacy (&a" + unverified.size() + "&7/&a" + this.core.getServer().getOnlinePlayers().size() + "&7): &a" + StringHelper.join(unverified, ", ")));

        if (!verified.isEmpty()) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Procent korzystajacych: &a" + verified.size() * 100 / this.core.getServer().getOnlinePlayers().size() + "%"));
        }
        return true;
    }
}
