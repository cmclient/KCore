package pl.kuezese.core.boss.stage.impl;

import io.papermc.lib.PaperLib;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.boss.Boss;
import pl.kuezese.core.boss.stage.Stage;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.RandomHelper;
import pl.kuezese.core.object.MobObject;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Stage3 extends Stage {

    private final Queue<Player> playerQueue = new ConcurrentLinkedQueue<>();
    private transient Location startLocation;
    private transient int teleportWaves = -1;

    public Stage3(Boss boss, double requiredHp, long delay, double mobMultiplier, MobObject... mobs) {
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
                    ChatHelper.title(other, "&8• &2&lBOSS &8•", "&7ROZPOCZYNA SIE &2&lFALA III&7...", 10, 40, 10);
                    other.playSound(other.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.0f);
                });


                this.setupMobQueue();
                this.cooldown = 30;
                this.waves = 450;
                this.teleportWaves = players.size() * 10;
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
            if (this.cooldown % 10 == 0) {
                players.forEach(other -> {
                    ChatHelper.title(other, "&8• &2&lBOSS &8•", "&2&l" + this.cooldown / 10 + "&7...", 10, 40, 10);
                    other.playSound(other.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                });
            }
            this.cooldown--;
            return;
        }

        // Check if there are any players
        if (this.waves > 0) {
            if (this.waves % 90 == 0) {
                if (this.startLocation == null) {
                    this.startLocation = bossLocation;
                }
                this.playerQueue.addAll(players);
                this.waves--;
                return;
            }

            if (this.waves % 50 == 0) {
                bossEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 50, 0));
                this.waves--;
                return;
            }

            // Check if attacking players is active
            if (!this.playerQueue.isEmpty()) {
                if (this.teleportWaves % 10 == 0) {
                    Player player = this.playerQueue.poll();

                    PaperLib.teleportAsync(bossEntity, player.getLocation()).thenAccept(result -> {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 80, 1));
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
                        players.forEach(other -> other.getWorld().playEffect(player.getLocation(), Effect.PORTAL, 10));
                        Creature creature = bossEntity instanceof Creature ? ((Creature) bossEntity) : null;
                        if (creature != null) {
                            creature.setTarget(player);
                        }
                        for (int i = 0; i < 8; i++) {
                            {
                                TNTPrimed tnt = player.getWorld().spawn(bossEntity.getEyeLocation(), TNTPrimed.class);
                                tnt.setVelocity(player.getLocation().getDirection().normalize().multiply(1));
                                tnt.setCustomName("BOSS TNT");
                            }
                            {
                                TNTPrimed tnt = player.getWorld().spawn(player.getLocation(), TNTPrimed.class);
                                tnt.setCustomName("BOSS TNT");
                            }
                        }
                    });
                }
                this.teleportWaves--;
                return;
            }

            if (this.startLocation != null) {
                PaperLib.teleportAsync(bossEntity, this.startLocation);
                this.startLocation = null;
                this.waves--;
                return;
            }

            players.forEach(player -> {
                // Launch a fireball towards the random player
                this.launchSnowballAtPlayer(bossEntity, player, 1.0D, 20.0D);

                // Play sound
                player.playSound(bossLocation, Sound.WITHER_SHOOT, 1.0f, 1.0f);
            });

            this.waves--;
            return;
        }

        this.running = false;
        this.cancel();
    }

    private void launchSnowballAtPlayer(LivingEntity boss, Player player, double offset, double damage) {
        Location bossLocation = boss.getEyeLocation();
        Location playerLocation = player.getEyeLocation();

        // Calculate direction vector from boss to player
        Vector direction = playerLocation.toVector().subtract(bossLocation.toVector()).normalize();

        // Calculate the new launch location slightly extended from the boss
        Location launchLocation = bossLocation.clone().add(direction.multiply(offset));

        // Spawn a snowball at the new launch location
        Snowball snowball = bossLocation.getWorld().spawn(launchLocation, Snowball.class);

        // Set the direction and speed of the snowball
        snowball.setVelocity(direction.multiply(1.5)); // Adjust the speed as needed

        // Set the snowball's shooter to the boss
        snowball.setShooter(boss);

        // Set the damage dealt by the snowball
        snowball.setCustomName("heavySnowball"); // You can use a custom name to identify the snowball
        snowball.setCustomNameVisible(false); // Set to true if you want the name to be visible
        snowball.setMetadata("damage", new FixedMetadataValue(CorePlugin.getInstance(), damage)); // Use plugin instance
    }
}
