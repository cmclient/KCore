package pl.kuezese.core.boss.stage.impl;

import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import pl.kuezese.core.boss.Boss;
import pl.kuezese.core.boss.stage.Stage;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.RandomHelper;
import pl.kuezese.core.object.MobObject;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Stage4 extends Stage {

    private final transient Queue<Player> playerQueue = new ConcurrentLinkedQueue<>();
    private transient Location startLocation;

    public Stage4(Boss boss, double requiredHp, long delay, double mobMultiplier, MobObject... mobs) {
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
                bossEntity.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 4));
                bossEntity.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 1));
                bossEntity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0));

                players.forEach(other -> {
                    ChatHelper.title(other, "&8• &2&lBOSS &8•", "&7TO JESZCZE &2&lNIE KONIEC&7...", 10, 40, 10);
                    other.playSound(other.getLocation(), Sound.WITHER_SPAWN, 1.0f, 1.0f);
                    other.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
                    other.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 4));
                    other.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 4));
                });

                for (int i = 0; i < 100; i++) {
                    Location location = RandomHelper.getRandomLocationAround(bossLocation, 7.5D);
                    TNTPrimed tnt = location.getWorld().spawn(location, TNTPrimed.class);
                    tnt.setCustomName("BOSS TNT");
                }

                this.setupMobQueue();
                this.cooldown = 15;
                this.running = true;
            }
            return;
        }

        if (this.cooldown > 0) {
            if (this.cooldown <= 12 && this.cooldown % 4 == 0) {
                players.forEach(other -> {
                    ChatHelper.title(other, "&8• &2&lBOSS &8•", "&2&l" + this.cooldown / 4 + "&7...", 10, 40, 10);
                    other.playSound(other.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                });
            }
            this.cooldown--;
            return;
        }

        if (this.cooldown == 0) {
            bossEntity.setHealth(bossEntity.getMaxHealth() * 0.25);
            players.forEach(player -> player.getLocation().getWorld().strikeLightning(player.getLocation()));

            this.startLocation = bossLocation;
            for (int i = 0; i < 4; i++) this.playerQueue.addAll(players);

            this.cooldown = -1;
            return;
        }

        if (!this.playerQueue.isEmpty()) {
            Player player = this.playerQueue.poll();
            PaperLib.teleportAsync(bossEntity, player.getLocation()).thenAccept(result -> {
                Creature creature = bossEntity instanceof Creature ? ((Creature) bossEntity) : null;
                if (creature != null) {
                    creature.setTarget(player);
                }
                player.damage(1.0F);
                this.throwPotions(player.getEyeLocation());
                this.throwPotions(player.getEyeLocation());
            });
            return;
        }

        if (this.startLocation != null) {
            PaperLib.teleportAsync(bossEntity, this.startLocation);
            this.startLocation = null;
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

        this.running = false;
        this.cancel();
    }

    private void throwPotions(Location location) {
        // Create Instant Damage II potion
        Potion builder = new Potion(PotionType.INSTANT_DAMAGE, 1);
        builder.setSplash(true);
        ItemStack damagePotion = builder.toItemStack(1);

        // Create Poison II potion
        Potion builder1 = new Potion(PotionType.POISON, 1);
        builder1.setSplash(true);
        ItemStack poisonPotion = builder1.toItemStack(1);

        // Apply Potion Effects
        ThrownPotion damageThrownPotion = (ThrownPotion) location.getWorld().spawnEntity(location, EntityType.SPLASH_POTION);
        damageThrownPotion.setItem(damagePotion);
        damageThrownPotion.getEffects().add(new PotionEffect(PotionEffectType.HARM, 1, 1));

        ThrownPotion poisonThrownPotion = (ThrownPotion) location.getWorld().spawnEntity(location, EntityType.SPLASH_POTION);
        poisonThrownPotion.setItem(poisonPotion);
        poisonThrownPotion.getEffects().add(new PotionEffect(PotionEffectType.POISON, 300, 1));
    }
}
