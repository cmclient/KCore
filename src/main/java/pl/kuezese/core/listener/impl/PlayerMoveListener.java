package pl.kuezese.core.listener.impl;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.listener.Listener;
import pl.kuezese.core.object.User;
import pl.kuezese.region.object.Region;

public class PlayerMoveListener extends Listener {

    private final Region spawn;
    private final Location center;
    private final int borderSize;

    public PlayerMoveListener() {
        this.spawn = this.regionManager.find("spawn", this.core.getServer().getWorlds().get(0));
        this.borderSize = this.core.getConfiguration().getBorderSize();
        this.center = calculateSpawnCenter();
    }

    private Location calculateSpawnCenter() {
        if (this.spawn != null) {
            Location loc = new Location(spawn.getWorld(), core.getConfiguration().getSpawnLoc().getX(), 0.0, core.getConfiguration().getSpawnLoc().getZ());
            loc.setY(loc.getWorld().getHighestBlockYAt(loc));
            return loc;
        }
        return null;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();

        if (hasMovedStrict(from, to)) {
            handleFlightRestriction(player, to);
        }

        if (hasMoved(from, to)) {
            handleTeleportInterruption(player);
            handleCombatRegion(player, to);
            handleBorder(player, to);
        }
    }

    private boolean hasMoved(Location from, Location to) {
        return from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ();
    }

    private boolean hasMovedStrict(Location from, Location to) {
        return from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ();
    }

    private void handleTeleportInterruption(Player player) {
        BukkitTask task = this.core.getTeleportManager().getTeleports().remove(player.getName());
        if (task != null) {
            task.cancel();
            player.removePotionEffect(PotionEffectType.CONFUSION);
            ChatHelper.title(player, "&8• &2&lTELEPORTACJA &8•", "&7Ruszyles sie&7, teleportacja zostala &cprzerwana&7!", 10, 40, 10);
        }
    }

    private void handleFlightRestriction(Player player, Location to) {
        if (player.getAllowFlight() && !player.hasPermission("cm.helpop") && this.regionManager.find(to) != this.spawn) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }
    }

    private void handleCombatRegion(Player player, Location to) {
        User user = this.userManager.get(player.getName());
        if (user.isCombat() && this.regionManager.find(to) == this.spawn) {
            Vector direction = center.toVector().subtract(player.getLocation().toVector()).normalize().multiply(-2).setY(0.8);
            player.setVelocity(direction);
        }
    }

    private void handleBorder(Player player, Location to) {
        if (isLocationOutBorder(to)) {
            Vector direction = getVectorSpawn(to).add(new Vector(0.0, 0.5, 0.0)).multiply(1.1);
            player.setVelocity(direction);
            player.playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 5.0F);
            ChatHelper.title(player, "&8\u2022 &cGRANICA &8\u2022", "&7Osiagnales &cgranice &7mapy!", 10, 40, 10);
        }
    }

    private boolean isLocationOutBorder(Location loc) {
        return Math.abs(loc.getBlockX()) > borderSize || Math.abs(loc.getBlockZ()) > borderSize;
    }

    private Vector getVectorSpawn(Location loc) {
        Location spawn = loc.getWorld().getSpawnLocation();
        Vector direction = spawn.toVector().subtract(loc.toVector());
        return direction.normalize();
    }
}
