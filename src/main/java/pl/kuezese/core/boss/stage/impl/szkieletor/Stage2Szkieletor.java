package pl.kuezese.core.boss.stage.impl.szkieletor;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import pl.kuezese.core.boss.Boss;
import pl.kuezese.core.boss.stage.Stage;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.RandomHelper;
import pl.kuezese.core.object.MobObject;

import java.util.List;

public class Stage2Szkieletor extends Stage {

    public Stage2Szkieletor(Boss boss, double requiredHp, long delay, int mobMultiplier, MobObject... mobs) {
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
                this.cooldown = 15;
                this.waves = Math.min(Math.max(players.size() * 12, 20), 40) * 5;
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
            if (this.cooldown % 5 == 0) {
                players.forEach(other -> {
                    ChatHelper.title(other, "&8• &2&lBOSS &8•", "&2&l" + this.cooldown / 5 + "&7...", 10, 40, 10);
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
                    this.shootArrow(bossEntity, player);

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

    private void shootArrow(LivingEntity shooterEntity, LivingEntity targetEntity) {
        Location bossLocation = shooterEntity.getEyeLocation().add(0, 1, 0);
        Location playerLocation = targetEntity.getEyeLocation();

        // Calculate direction vector from boss to player
        Vector direction = playerLocation.toVector().subtract(bossLocation.toVector()).normalize();

        // Spawn arrow
        Arrow arrow = shooterEntity.launchProjectile(Arrow.class, direction);

        arrow.setVelocity(direction.multiply(1.5)); // Adjust the speed as needed
        arrow.setShooter(shooterEntity);

        arrow.setCritical(true);
        arrow.setKnockbackStrength(1);
    }
}
