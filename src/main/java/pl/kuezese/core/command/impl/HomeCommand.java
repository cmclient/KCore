package pl.kuezese.core.command.impl;

import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import pl.kuezese.core.command.Command;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.StringHelper;
import pl.kuezese.core.object.User;

public class HomeCommand extends Command {

    public HomeCommand() {
        super("home", "");
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        Player p = sender instanceof Player ? ((Player) sender) : null;

        if (p == null) {
            sender.sendMessage("Ta komende mozna uzyc tylko z poziomu gracza.");
            return true;
        }

        User user = this.userManager.get(p.getName());
        Location home = user.getHome();

        if (home == null) {
            p.sendMessage(ChatHelper.color(" &8>> &7Nie posiadasz ustawionego domu."));
            return true;
        }

        if (home.getY() <= 0) {
            p.sendMessage(ChatHelper.color(" &8>> &cNieprawidlowa lokalizacja domu."));
            return true;
        }

        if (this.core.getTeleportManager().getTeleports().containsKey(p.getName())) this.core.getTeleportManager().getTeleports().remove(p.getName()).cancel();

        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 160, 0, true));

        this.core.getTeleportManager().getTeleports().put(p.getName(), new BukkitRunnable() {

            private int timer = p.hasPermission("cm.fasterteleport") ? 5 : 6;

            @Override
            public void run() {
                timer--;
                ChatHelper.title(p, "&8• &2&lTELEPORTACJA &8•", "&7Zostaniesz przeteleportowany za &a" + timer + " &7" + StringHelper.pluralizedTimeText(timer) + ".", 0, 30, 0);
                if (timer == 0) {
                    p.removePotionEffect(PotionEffectType.CONFUSION);
                    PaperLib.teleportAsync(p, home)
                                    .thenAccept(result -> ChatHelper.title(p, "&8• &2&lTELEPORTACJA &8•", "&7Zostales przeteleportowany.", 10, 40, 10));
                    (core.getTeleportManager().getTeleports().remove(p.getName())).cancel();
                }
            }
        }.runTaskTimer(this.core, 0L, 20L));
        return true;
    }
}
