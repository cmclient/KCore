package pl.kuezese.core.boss.stage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.boss.Boss;
import pl.kuezese.core.object.MobObject;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class Stage implements Runnable {

    protected final Boss boss;
    protected final double requiredHp;
    protected final long delay;
    protected final double mobMultiplier;
    protected final MobObject[] mobs;
    protected boolean running;
    protected int cooldown = -1;
    public int waves = -1;
    protected Queue<MobObject> mobsQueue = new ConcurrentLinkedQueue<>();
    private @Getter BukkitTask task;
    protected CorePlugin core = CorePlugin.getInstance();

    protected void setupMobQueue() {
        for (int i = 0; i < this.boss.getEntity().getWorld().getPlayers().size() * this.mobMultiplier; i++) {
            this.mobsQueue.addAll(Arrays.asList(this.mobs));
        }
    }

    public void start(boolean async) {
        this.cancel();
        this.task = async ? this.core.getServer().getScheduler().runTaskTimerAsynchronously(this.core, this, 20L, this.delay)
                : this.core.getServer().getScheduler().runTaskTimer(this.core, this, 20L, this.delay);
    }

    protected void cancel() {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }
    }


    protected List<Player> getPlayersInRadius(Location location, double radius) {
        return location.getWorld().getPlayers()
                .stream()
                .filter(player -> !player.isDead())
                .filter(player -> !player.isFlying())
                .filter(player -> player.getGameMode() == GameMode.SURVIVAL)
                .filter(player -> player.getLocation().distance(location) <= radius)
                .collect(Collectors.toList());
    }

    protected void launchFireballAtPlayer(LivingEntity boss, Player player, double offset) {
        Location bossLocation = boss.getEyeLocation();
        Location playerLocation = player.getEyeLocation();

        // Calculate direction vector from boss to player
        Vector direction = playerLocation.toVector().subtract(bossLocation.toVector()).normalize();

        // Calculate the new launch location slightly extended from the boss
        Location launchLocation = bossLocation.clone().add(direction.multiply(offset));

        // Spawn a fireball at the new launch location
        Fireball fireball = bossLocation.getWorld().spawn(launchLocation, Fireball.class);

        // Set the direction and speed of the fireball
        fireball.setDirection(direction);
        fireball.setVelocity(direction.multiply(1.5)); // Adjust the speed as needed

        // Set the fireball's shooter to the boss
        fireball.setShooter(boss);
    }
}
