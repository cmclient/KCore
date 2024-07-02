package pl.kuezese.core.boss.stage.impl;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import pl.kuezese.core.boss.Boss;
import pl.kuezese.core.boss.stage.Stage;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.RandomHelper;
import pl.kuezese.core.object.MobObject;

import java.util.List;

public class Stage2 extends Stage {

    public Stage2(Boss boss, double requiredHp, long delay, double mobMultiplier, MobObject... mobs) {
        super(boss, requiredHp, delay, mobMultiplier, mobs);
    }

    @Override
    public void run() {
        if (!this.boss.isAlive()) {
            this.running = false;
            this.cancel();
            return;
        }

        // Get boss entity
        LivingEntity bossEntity = this.boss.getEntity();

        // Get boss location
        Location bossLocation = bossEntity.getLocation();

        // Get players within a certain radius of the boss
        double radius = 50.0;

        // Use your method to get players within the radius
        List<Player> players = this.getPlayersInRadius(bossLocation, radius);

        if (players.isEmpty()) {
            return;
        }

        if (!this.running) {
            double healthPercent = (bossEntity.getHealth() / bossEntity.getMaxHealth()) * 100;
            if (healthPercent < this.requiredHp) {
                players.forEach(other -> {
                    ChatHelper.title(other, "&8• &2&lBOSS &8•", "&7ROZPOCZYNA SIE &2&lFALA II&7...", 10, 40, 10);
                    other.playSound(other.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.0f);
                });

                this.setupMobQueue();
                this.cooldown = 6;
                this.waves = Math.min(Math.max(players.size() * 8, 20), 40) * 2;
                this.running = true;
            }
            return;
        }

        // Spawn mobs
        if (!this.mobsQueue.isEmpty()) {
            // Create loaction around boss
            Location location = RandomHelper.getRandomLocationAround(bossLocation, 7.5D);

            // Poll mob object from the queue
            MobObject mobObject = this.mobsQueue.poll();

            // Spawn mob object
            mobObject.spawn(this.boss, location, players);
            return;
        }

        if (this.cooldown > 0) {
            if (this.cooldown % 2 == 0) {
                players.forEach(other -> {
                    ChatHelper.title(other, "&8• &2&lBOSS &8•", "&2&l" + this.cooldown / 2 + "&7...", 10, 40, 10);
                    other.playSound(other.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                });
            }
            this.cooldown--;
            return;
        }

        // Check if there are any players to attack
        if (this.waves > 0) {
            List<Player> attackPlayers = this.getPlayersInRadius(bossLocation, 10.0f);

            if (!attackPlayers.isEmpty()) {
                attackPlayers.forEach(player -> {
                    // Launch a fireball towards the random player
                    this.launchFireballAtPlayer(bossEntity, player, 1.0D);

                    // Play sound
                    player.playSound(bossLocation, Sound.WITHER_SHOOT, 1.0f, 1.0f);
                });
                this.waves--;
            }

            return;
        }

        this.running = false;
        this.cancel();
    }
}
