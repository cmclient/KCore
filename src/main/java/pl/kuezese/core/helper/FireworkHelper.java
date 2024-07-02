package pl.kuezese.core.helper;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public final class FireworkHelper {

    public static void spawn(Location location) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkEffect effect = FireworkEffect.builder().withColor(Color.GREEN).withFade(Color.WHITE).with(FireworkEffect.Type.BALL).trail(true).build();
        FireworkMeta fwm = firework.getFireworkMeta();
        fwm.addEffect(effect);
        firework.setFireworkMeta(fwm);
    }
}
