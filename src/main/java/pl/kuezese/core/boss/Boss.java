package pl.kuezese.core.boss;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import pl.kuezese.core.CorePlugin;
import pl.kuezese.core.boss.controller.BossControllerTask;
import pl.kuezese.core.boss.stage.Stage;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.RandomHelper;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public @Getter @Setter class Boss {

    private final CorePlugin core;
    private final String type;
    private final String name;
    private final double health;
    private final EntityType entityType;
    private final boolean baby;
    private final List<ItemStack> armor;
    private final Map<ItemStack, Double> drops;
    private final List<PotionEffect> effects;
    private final Stage controllerTask = new BossControllerTask(this);
    private final List<Stage> stages = new LinkedList<>();
    private LivingEntity entity;
    private ItemStack itemInHand;
    private final List<LivingEntity> allies;

    public Boss(CorePlugin core, String type, String name, double health, EntityType entityType, boolean baby, List<PotionEffect> effects) {
        this.core = core;
        this.type = type;
        this.name = ChatHelper.color(name);
        this.entityType = entityType;
        this.health = health;
        this.baby = baby;
        this.armor = new LinkedList<>();
        this.allies = new CopyOnWriteArrayList<>();
        this.drops = new LinkedHashMap<>();
        this.effects = effects;
    }

    public Location spawn(World world) {
        Location location = RandomHelper.randomLoc(world, 2000);
        location.getWorld().loadChunk(location.getChunk());
        this.entity = (LivingEntity) world.spawnEntity(location, this.entityType);
        this.entity.setCustomName(this.name);
        this.entity.setCustomNameVisible(true);
        this.entity.setMaxHealth(this.health);
        this.entity.setHealth(this.entity.getMaxHealth());
        this.entity.setCanPickupItems(false);
        if (this.itemInHand != null) {
            this.entity.getEquipment().setItemInHand(this.itemInHand);
            this.entity.getEquipment().setItemInHandDropChance(0.0F);
        }
        if (!this.armor.isEmpty()) {
            this.entity.getEquipment().setArmorContents(this.armor.toArray(new ItemStack[0]));
        }
        if (this.entity instanceof Zombie) {
            Zombie zombie = (Zombie) this.entity;
            zombie.setBaby(this.baby);
        }
        if (this.entity instanceof Creeper) {
            Creeper creeper = (Creeper) this.entity;
            creeper.setPowered(this.baby);
        }
        if (this.entity instanceof Skeleton) {
            Skeleton skeleton = (Skeleton) this.entity;
            skeleton.setSkeletonType(this.baby ? Skeleton.SkeletonType.WITHER : Skeleton.SkeletonType.NORMAL);
        }
        this.effects.forEach(potionEffect -> this.entity.addPotionEffect(potionEffect));
        this.controllerTask.start(false);
        this.stages.forEach(stage -> stage.start(false));
        return location;
    }

    public void kill() {
        this.allies.forEach(ally -> {
//            ally.damage(this.entity.getHealth());
            ally.setHealth(0.0D);
            ally.remove();
        });

        if (this.entity != null) {
//            this.entity.damage(this.entity.getHealth());
            this.entity.setHealth(0.0D);
            this.entity.remove();
        }
    }

    public boolean isAlive() {
        return this.entity != null && this.entity.getHealth() > 0.0D;
    }
}
