package pl.kuezese.core.boss.controller;

import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import pl.kuezese.core.boss.Boss;
import pl.kuezese.core.boss.stage.Stage;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.MobObject;

public class BossControllerTask extends Stage {

    public BossControllerTask(Boss boss) {
        super(boss, 0.0D, 5L, 0, new MobObject[]{});
    }

    @Override
    public void run() {
        if (!this.boss.isAlive()) {
            this.core.getServer().getOnlinePlayers().forEach(player -> this.core.getBossy().hide(player));
            this.running = false;
            this.cancel();
            return;
        }

        this.boss.getAllies().removeIf(LivingEntity::isDead);
        this.boss.getAllies().stream().filter(ally -> ally instanceof Creature).forEach(ally -> {
            Creature creature = (Creature) ally;
            Player nearestPlayer = this.getNearestPlayer(ally.getLocation(), 50.0D);
            if (nearestPlayer != null) {
                creature.setTarget(nearestPlayer);
            }
        });

        LivingEntity bossEntity = this.boss.getEntity();
        Location bossLocation = bossEntity.getLocation();

        if (bossEntity instanceof Creature) {
            Creature creature = (Creature) this.boss.getEntity();
            Player nearestPlayer = this.getNearestPlayer(bossLocation, 50.0D);
            if (nearestPlayer != null) {
                creature.setTarget(nearestPlayer);
            }
        }

        double health = this.boss.getEntity().getHealth();
        double maxHealth = this.boss.getEntity().getMaxHealth();
        float healthPercent = (float) (health / maxHealth);

        this.core.getServer().getOnlinePlayers().forEach(player -> {
            String message;
            if (player.getLocation().distance(bossLocation) > 20.0D) {
                int x = bossLocation.getBlockX();
                int y = bossLocation.getBlockY();
                int z = bossLocation.getBlockZ();
                message = ChatHelper.color("&7Boss &9" + StringUtils.capitalize(ChatColor.stripColor(boss.getName().toLowerCase())) + " &7- X: &9" + x + " &7Y: &9" + y + " &7Z: &9" + z);
            } else {
                message = ChatHelper.color("&7Boss &9" + StringUtils.capitalize(ChatColor.stripColor(boss.getName().toLowerCase())) + " &7- HP: &9" + (int) health + "/" + (int) maxHealth + "❤");
            }
            this.core.getBossy().set(player, message, healthPercent);
        });
    }

    private Player getNearestPlayer(Location location, double radius) {
        Player nearestPlayer = null;
        double nearestDistanceSquared = Double.MAX_VALUE;

        for (Player player : this.core.getServer().getOnlinePlayers()) {
            if (player.isDead() || player.isFlying() || player.getGameMode() != GameMode.SURVIVAL)
                continue;

            Location playerLocation = player.getLocation();
            double distanceSquared = location.distanceSquared(playerLocation);

            if (distanceSquared <= radius * radius && distanceSquared < nearestDistanceSquared) {
                nearestPlayer = player;
                nearestDistanceSquared = distanceSquared;
            }
        }

        return nearestPlayer;
    }
}
