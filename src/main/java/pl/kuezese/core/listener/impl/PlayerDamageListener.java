package pl.kuezese.core.listener.impl;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import pl.kuezese.core.listener.Listener;
import pl.kuezese.core.object.User;

public class PlayerDamageListener extends Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSnowballDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damaged = event.getEntity();

        // Check if the damage is caused by a snowball
        if (damager instanceof Snowball && damaged instanceof Player) {
            Snowball snowball = (Snowball) damager;

            // Check if the snowball has the custom name "heavySnowball"
            if (snowball.getCustomName() != null && snowball.getCustomName().equals("heavySnowball")) {
                // Retrieve the damage value from the snowball's metadata
                if (snowball.hasMetadata("damage")) {
                    double damage = snowball.getMetadata("damage").get(0).asDouble();

                    // Apply the custom damage to the player
                    event.setDamage(damage);

                    // Optionally, you can remove the snowball after it hits the player
                    snowball.remove();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExplosionDamage(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();
        Entity damagerEntity = event.getDamager();

        // Check if the damager is a TNTPrimed with the custom name "BOSS TNT" and the damaged entity is a Zombie
        if (damagerEntity instanceof TNTPrimed && damagedEntity instanceof Creature) {
            TNTPrimed tnt = (TNTPrimed) damagerEntity;

            if ("BOSS TNT".equals(tnt.getCustomName())) {
                // Cancel the damage event
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();
            Player damager = this.getDamager(e);

            if (damager != null) {
                if (!damager.hasPermission("cm.bypass")) {
                    damager.setAllowFlight(false);
                }

                if (!victim.hasPermission("cm.bypass")) {
                    victim.setAllowFlight(false);
                }

                User user = this.userManager.get(victim.getName());
                user.setAttackTime(System.currentTimeMillis() + 30000L);
                user.setDamager(damager);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();
            User user = this.userManager.get(victim.getName());

            if (user.isGodmode()) {
                e.setCancelled(true);
            }
        }
    }

    private Player getDamager(EntityDamageByEntityEvent e) {
        Entity entity = e.getDamager();

        if (entity instanceof Player) {
            return (Player) entity;
        }

        if (entity instanceof Projectile) {
            Projectile projectile = (Projectile) entity;
            if (projectile.getShooter() instanceof Player) {
                return (Player) projectile.getShooter();
            }
        }
        return null;
    }
}
