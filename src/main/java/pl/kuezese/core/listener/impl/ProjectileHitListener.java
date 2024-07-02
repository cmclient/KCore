package pl.kuezese.core.listener.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.listener.Listener;

import java.text.DecimalFormat;

public class ProjectileHitListener extends Listener {

    private final DecimalFormat hpFormat = new DecimalFormat("#.##");

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) e.getDamager();
            if (projectile.getShooter() instanceof Player && e.getEntity() instanceof Player) {
                Player victim = (Player) e.getEntity();
                Player shooter = (Player) projectile.getShooter();
                if (victim != shooter) {
                    shooter.sendMessage(ChatHelper.color(" &8>> &7Gracz &a" + victim.getName() + " &7posiada &c" + this.hpFormat.format(victim.getHealth()) + "\u2764&7."));
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            Player player = e.getPlayer();
            double v = player.getWorld().getWorldBorder().getSize() / 2.0;
            Location center = player.getWorld().getWorldBorder().getCenter();
            if (center.getX() + v < e.getTo().getX()
                    || center.getX() - v > e.getTo().getX()
                    || center.getZ() + v < e.getTo().getZ()
                    || center.getZ() - v > e.getTo().getZ()) {
                e.setCancelled(true);
                player.sendMessage(ChatHelper.color(" &8>> &cNie mozesz przeteleportowac sie poza granice mapy."));
                return;
            }

            e.setCancelled(true);
            player.teleport(e.getTo());
            player.setFallDistance(0.0F);
        }
    }
}
