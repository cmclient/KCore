package pl.kuezese.core.listener.impl;

import com.google.common.base.Strings;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.boss.Boss;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.FireworkHelper;
import pl.kuezese.core.helper.RandomHelper;
import pl.kuezese.core.listener.Listener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BossListener extends Listener {

    private final DecimalFormat format = new DecimalFormat("#.##");

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (event.getPlayer().isOp())
            return;

        Boss boss = this.core.getBossManager().getCurrentBoss();
        if (boss == null)
            return;

        if (event.getBlockPlaced().getLocation().distance(boss.getEntity().getLocation()) < 25) {
            event.setCancelled(true);
        }
    }


    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().isOp())
            return;

        Boss boss = this.core.getBossManager().getCurrentBoss();
        if (boss == null)
            return;

        if (event.getBlock().getLocation().distance(boss.getEntity().getLocation()) < 25) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        Boss boss = this.core.getBossManager().getCurrentBoss();
        if (boss == null)
            return;

        LivingEntity entity = event.getEntity();
        if (entity == boss.getEntity()) {
            Location location = boss.getEntity().getLocation();
            StringBuilder builder = new StringBuilder();

            List<Map.Entry<ItemStack, Double>> drops = new ArrayList<>(boss.getDrops().entrySet());
            Collections.shuffle(drops);

            drops.stream().filter(entry -> RandomHelper.chance(entry.getValue())).limit(4).forEach(entry -> {
                ItemStack is = entry.getKey();
                double chance = entry.getValue();
                location.getWorld().dropItem(location, is);
                String customName = Strings.isNullOrEmpty(is.getItemMeta().getDisplayName()) ? is.getType().name() : is.getItemMeta().getDisplayName()
                        .replace(ChatHelper.color("&8>> "), "")
                        .trim();
                builder.append(ChatHelper.color(" &7>> &ax" + is.getAmount() + " " + customName + " &8(&a" + this.format.format(chance) + "%&8)\n"));
            });

            String killer = entity.getKiller() == null ? "?" : entity.getKiller().getName();

            this.core.getServer().broadcastMessage(ChatHelper.color(" &8&m------------------------------"));
            this.core.getServer().broadcastMessage("");
            this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Boss &9" + StringUtils.capitalize(ChatColor.stripColor(boss.getName().toLowerCase())) + " &7zostal zabity!"));
            this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Przez gracza: &a" + killer));
            String msg = builder.toString();
            if (!msg.isEmpty()) {
                this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Wylosowane przedmioty:"));
                this.core.getServer().broadcastMessage(msg);
            }
            this.core.getServer().broadcastMessage("");
            this.core.getServer().broadcastMessage(ChatHelper.color(" &8&m------------------------------"));

            for (int i = 0; i < 8; i++) FireworkHelper.spawn(location);

            this.core.getServer().getOnlinePlayers().forEach(player -> {
                ChatHelper.title(player, "&8• &2&lBOSS &8•", "&7Boss &9" + StringUtils.capitalize(ChatColor.stripColor(boss.getName().toLowerCase())) + " &7zostal zabity przez: &a" + killer, 10, 30, 10);
                player.playSound(player.getLocation(), Sound.WITHER_DEATH, 1.0F, 1.0F);
            });

            boss.getAllies().forEach(ally -> {
                ally.damage(ally.getHealth());
                ally.setHealth(0.0D);
                ally.remove();
            });

            this.core.getBossManager().setCurrentBoss(null);
        }
    }
}
