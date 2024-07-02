package pl.kuezese.core.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import pl.kuezese.core.boss.Boss;
import pl.kuezese.core.helper.RandomHelper;

import java.util.List;

/**
 * Represents a mob object in the TikTokGame plugin, with a specific EntityType and an optional name.
 */
@RequiredArgsConstructor @Getter
public class MobObject {

    private final EntityType type;
    private final  String name;
    private final boolean secondary;

    public boolean isBaby() {
        return this.secondary || RandomHelper.nextBoolean();
    }

    public Skeleton.SkeletonType getSkeletonType() {
        return this.secondary ? Skeleton.SkeletonType.WITHER : Skeleton.SkeletonType.NORMAL;
    }

    public void spawn(Boss boss, Location location, List<Player> players) {
        LivingEntity mob = (LivingEntity) location.getWorld().spawn(location, this.type.getEntityClass());

        mob.setCustomName(this.name);
        mob.setCustomNameVisible(true);

        switch (mob.getType()) {
            case ZOMBIE: {
                Zombie zombie = (Zombie) mob;
                zombie.setBaby(this.isBaby());
                break;
            }
            case SKELETON: {
                Skeleton skeleton = (Skeleton) mob;
                skeleton.setSkeletonType(this.getSkeletonType());
                break;
            }
            case CREEPER: {
                Creeper creeper = (Creeper) mob;
                creeper.setPowered(this.isBaby());
                break;
            }
        }

        mob.getEquipment().setItemInHandDropChance(0.0F);
        mob.getEquipment().setHelmetDropChance(0.0F);
        mob.getEquipment().setChestplateDropChance(0.0F);
        mob.getEquipment().setLeggingsDropChance(0.0F);
        mob.getEquipment().setBootsDropChance(0.0F);

        mob.setCanPickupItems(false);
        boss.getAllies().add(mob);
        location.getWorld().playEffect(location, Effect.EXPLOSION_LARGE, 2);
        players.forEach(other -> other.playSound(mob.getLocation(), Sound.ITEM_PICKUP, 1.0f, 1.0f));
    }
}
