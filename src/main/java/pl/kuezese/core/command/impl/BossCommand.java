package pl.kuezese.core.command.impl;

import io.papermc.lib.PaperLib;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.kuezese.region.helper.StringHelper;
import pl.kuezese.core.boss.Boss;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;

public class BossCommand extends Command {

    public BossCommand() {
        super("boss", "cm.boss");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        Player player = sender instanceof Player ? ((Player) sender) : null;

        if (player == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/boss <spawn/kill/tp/tp/info/list> [typ/random]"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "spawn": {
                Boss boss = this.core.getBossManager().getCurrentBoss();

                if (boss != null && boss.isAlive()) {
                    sender.sendMessage(ChatHelper.color(" &8>> &cJuz jest boss."));
                    return true;
                }

                String bossType = args.length == 1 ? null : args[1];

                if (bossType == null) {
                    sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/boss <spawn/kill/tp/tphere/info/list> <typ/random>"));
                    return true;
                }

                if (bossType.equalsIgnoreCase("random")) {
                    this.core.getBossManager().spawn();
                    break;
                }

                Boss bossToSpawn = this.core.getBossManager().get(bossType);

                if (bossToSpawn == null) {
                    sender.sendMessage(ChatHelper.color(" &8>> &cNie ma takiego bossa."));
                    return true;
                }

                this.core.getBossManager().spawn(bossToSpawn);
                break;
            }
            case "kill": {
                Boss boss = this.core.getBossManager().getCurrentBoss();

                if (boss == null || !boss.isAlive()) {
                    sender.sendMessage(ChatHelper.color(" &8>> &cObecnie nie ma zadnego bossa."));
                    return true;
                }

                this.core.getBossManager().kill();
                sender.sendMessage(ChatHelper.color(" &8>> &aBoss zostal zabity."));
                break;
            }
            case "tp": {
                Boss boss = this.core.getBossManager().getCurrentBoss();

                if (boss == null || !boss.isAlive()) {
                    sender.sendMessage(ChatHelper.color(" &8>> &cObecnie nie ma zadnego bossa."));
                    return true;
                }

                PaperLib.teleportAsync(player, boss.getEntity().getLocation())
                        .thenAccept(result -> sender.sendMessage(ChatHelper.color(" &8>> &aPrzeteleportowano do bossa.")));
                break;
            }
            case "tphere": {
                Boss boss = this.core.getBossManager().getCurrentBoss();

                if (boss == null || !boss.isAlive()) {
                    sender.sendMessage(ChatHelper.color(" &8>> &cObecnie nie ma zadnego bossa."));
                    return true;
                }

                PaperLib.teleportAsync(boss.getEntity(), player.getLocation())
                        .thenAccept(result -> sender.sendMessage(ChatHelper.color(" &8>> &aPrzeteleportowano bossa do twojej lokalizacji.")));
                break;
            }
            case "info": {
                Boss boss = this.core.getBossManager().getCurrentBoss();

                if (boss == null || !boss.isAlive()) {
                    sender.sendMessage(ChatHelper.color(" &8>> &cObecnie nie ma zadnego bossa."));
                    return true;
                }

                sender.sendMessage(ChatHelper.color(" &8>> &7Aktualny boss: &f" + StringUtils.capitalize(boss.getType())));
                break;
            }
            case "list": {
                String bosses = StringHelper.join(this.core.getBossManager().getTypes(), ", ");
                sender.sendMessage(ChatHelper.color(" &8>> &7Dostepne bossy: &f" + bosses));
                break;
            }
            default: {
                sender.sendMessage(ChatHelper.color(" &8>> &7Poprawne uzycie &a/boss <spawn/kill/tp/tphere/info/list> [typ]"));
            }
        }
        return true;
    }
}