package pl.kuezese.core.command.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

import java.util.Comparator;

public class PingCommand extends Command {

    public PingCommand() {
        super("ping", "", "latency", "ms");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                CraftPlayer cp = (CraftPlayer) sender;

                int ping = cp.getHandle().ping;
                sender.sendMessage(ChatHelper.color(" &8>> &7Twoj ping wynosi: &a" + ping + "ms&7."));
                if (ping < 15) {
                    sender.sendMessage(ChatHelper.color(" &8>> &aTwoj ping jest idealny, nie powinienes miec lagow."));
                } else if (ping < 30) {
                    sender.sendMessage(ChatHelper.color(" &8>> &aTwoj ping jest niski, nie powinienes miec lagow."));
                } else if (ping < 50) {
                    sender.sendMessage(ChatHelper.color(" &8>> &aTwoj ping jest wysoki, mozesz miec problemy z gra."));
                } else {
                    sender.sendMessage(ChatHelper.color(" &8>> &cTwoj ping jest bardzo wysoki, bedziesz miec problemy z gra."));
                }
            } else {
                CraftPlayer cp = (CraftPlayer) this.core.getServer().getPlayer(args[0]);

                if (cp == null) {
                    sender.sendMessage(ChatHelper.color(" &8>> &cPodany gracz jest offline."));
                    return true;
                }

                int ping = cp.getHandle().ping;
                sender.sendMessage(ChatHelper.color(" &8>> &7Ping gracza " + cp.getName() + " wynosi: &a" + ping + "ms&7."));
                if (ping < 15) {
                    sender.sendMessage(ChatHelper.color(" &8>> &aPing gracza " + cp.getName() + " jest idealny, nie powinien miec lagow."));
                } else if (ping < 30) {
                    sender.sendMessage(ChatHelper.color(" &8>> &aPing gracza " + cp.getName() + " jest niski, nie powinien miec lagow."));
                } else if (ping < 50) {
                    sender.sendMessage(ChatHelper.color(" &8>> &aPing gracza " + cp.getName() + " jest wysoki, moze miec problemy z gra."));
                } else {
                    sender.sendMessage(ChatHelper.color(" &8>> &cPing gracza " + cp.getName() + " jest bardzo wysoki, bedzie miec problemy z gra."));
                }
            }
        }

        int average = this.core.getServer().getOnlinePlayers().stream().mapToInt(player -> ((CraftPlayer) player).getHandle().ping).sum() / this.core.getServer().getOnlinePlayers().size();

        CraftPlayer lowest = (CraftPlayer) this.core.getServer().getOnlinePlayers()
                .stream()
                .min(Comparator.comparingInt(player -> ((CraftPlayer) player).getHandle().ping))
                .orElse(null);

        CraftPlayer highest = (CraftPlayer) this.core.getServer().getOnlinePlayers()
                .stream()
                .max(Comparator.comparingInt(player -> ((CraftPlayer) player).getHandle().ping))
                .orElse(null);

        sender.sendMessage(ChatHelper.color(" &8>> &7Sredni ping na serwerze: &a" + average + "ms&7."));
        if (lowest != null) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Najnizszy ping na serwerze: &a" + lowest.getHandle().ping + "ms&7. (&a" + lowest.getName() + "&7)"));
        }
        if (highest != null) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Najwyzszy ping na serwerze: &a" + highest.getHandle().ping + "ms&7. (&a" + highest.getName() + "&7)"));
        }
        return true;
    }
}

