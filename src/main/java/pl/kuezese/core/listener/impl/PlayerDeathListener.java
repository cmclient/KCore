package pl.kuezese.core.listener.impl;

import net.dzikoysk.funnyguilds.FunnyGuilds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.RandomHelper;
import pl.kuezese.core.listener.Listener;
import pl.kuezese.core.manager.impl.KitManager;
import pl.kuezese.core.object.User;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PlayerDeathListener extends Listener {

    private final FunnyGuilds funnyGuilds = FunnyGuilds.getInstance();

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        Player damager = victim.getKiller();
        User user = this.userManager.get(victim.getName());

        user.setAttackTime(0L);
        user.setDamager(null);

        if (damager != null) {
            User user1 = this.userManager.get(damager.getName());
            boolean sameIp = damager.getAddress().getHostString().equalsIgnoreCase(victim.getAddress().getHostString());

            float add = BigDecimal.valueOf(sameIp ? 0 : damager.hasPermission("cm.morecoins") ? RandomHelper.nextDouble(5.0, 30.0) : RandomHelper.nextDouble(2.5, 15.0))
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();

            float remove = BigDecimal.valueOf(sameIp ? 0 : RandomHelper.nextDouble(1.0, 5.0))
                    .setScale(2, RoundingMode.HALF_UP)
                    .floatValue();

            user1.addCoins(add);

            if (user.getCoins() > remove) {
                user.removeCoins(remove);
            }

            net.dzikoysk.funnyguilds.user.User fgVictimUser = this.funnyGuilds.getUserManager().findByUuid(victim.getUniqueId()).get();
            net.dzikoysk.funnyguilds.user.User fgDamagerUser = this.funnyGuilds.getUserManager().findByUuid(damager.getUniqueId()).get();

            ChatHelper.title(damager, "&8• &2&lZABOJSTWO &8•", "&8*** " + (fgVictimUser.getGuild().isEmpty() ? "" : "&8[&c" + fgVictimUser.getGuild().get().getTag() + "&8] ") + "&7" + victim.getName() + " &8| &7+" + add + "$ coins &8***", 30, 40, 50);
            ChatHelper.title(victim, "&8• &2&lSMIERC &8•", "&8*** " + (fgDamagerUser.getGuild().isEmpty() ? "" : "&8[&c" + fgDamagerUser.getGuild().get().getTag() + "&8] ") + "&7" + damager.getName() + " &8| &7-" + remove + "$ coins &8***", 30, 40, 50);

            if (sameIp) {
                damager.sendMessage(ChatHelper.color(" &8>> &7Zabiles gracza o takim samym IP, coinsy nie zostaly dodane."));
                victim.sendMessage(ChatHelper.color(" &8>> &7Zabil cie gracz o takim samym IP, coinsy nie zostaly zabrane."));
            }

            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwner(victim.getName());
            head.setItemMeta(meta);
            e.getDrops().add(head);
            victim.playSound(victim.getLocation(), Sound.ENDERDRAGON_HIT, 1.0F, 1.0F);
            return;
        }

        switch (victim.getLastDamageCause().getCause()) {
            case FALL: {
                e.setDeathMessage(ChatHelper.color(" &8>> &7Gracz &a" + victim.getName() + " &7zgubil spadochron."));
                break;
            }
            case FIRE: {
                e.setDeathMessage(ChatHelper.color(" &8>> &7Gracz &a" + victim.getName() + " &7spalil sie."));
                break;
            }
            case DROWNING: {
                e.setDeathMessage(ChatHelper.color(" &8>> &7Gracz &a" + victim.getName() + " &7utonal."));
                break;
            }
            case LAVA: {
                e.setDeathMessage(ChatHelper.color(" &8>> &7Gracz &a" + victim.getName() + " &7probowal plywac w lawie."));
                break;
            }
            case MAGIC: {
                e.setDeathMessage(ChatHelper.color(" &8>> &7Gracz &a" + victim.getName() + " &7zostal zabity przy uzyciu magii."));
                break;
            }
            case SUFFOCATION: {
                e.setDeathMessage(ChatHelper.color(" &8>> &7Gracz &a" + victim.getName() + " &7udusil sie."));
                break;
            }
            case VOID: {
                e.setDeathMessage(ChatHelper.color(" &8>> &7Gracz &a" + victim.getName() + " &7wypadl poza swiat."));
                break;
            }
            default: {
                e.setDeathMessage(ChatHelper.color(" &8>> &7Gracz &a" + victim.getName() + " &7zginal."));
                break;
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        KitManager.Kit kit = core.getKitManager().getKits().get(0);
        if (kit != null) {
            ItemStack[] items = kit.getItems().stream().map(KitManager.Item::getItemStack).toArray(ItemStack[]::new);
            p.getInventory().addItem(items);
        }
    }
}
