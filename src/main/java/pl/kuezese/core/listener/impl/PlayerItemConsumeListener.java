package pl.kuezese.core.listener.impl;

import io.papermc.lib.PaperLib;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.listener.Listener;

import java.util.Comparator;

public class PlayerItemConsumeListener extends Listener {

    @EventHandler(ignoreCancelled = true)
    public void onConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();

        if (e.getItem().getType() == Material.GOLDEN_APPLE && e.getItem().getDurability() == 0) {
            p.removePotionEffect(PotionEffectType.ABSORPTION);
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
            return;
        }

        if (e.getItem().getType() == Material.MILK_BUCKET && p.getItemInHand().getItemMeta().getDisplayName() != null && p.getItemInHand().getItemMeta().getDisplayName().equals(ChatHelper.color("&8>> &aAnty-Nogi"))) {
            e.setCancelled(true);

            if (p.getItemInHand().getAmount() > 1)
                p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
            else p.setItemInHand(null);

            Location location = p.getWorld().getPlayers()
                    .stream()
                    .filter(player -> player != p)
                    .map(Player::getLocation)
                    .filter(loc -> loc.distance(p.getLocation()) <= 5)
                    .min(Comparator.comparingDouble(loc -> loc.distance(p.getLocation())))
                    .orElse(null);

            if (location == null) {
                p.sendMessage(ChatHelper.color(" &8>> &cNie masz sie do kogo przeteleportowac."));
                return;
            }

            PaperLib.teleportAsync(p, location)
                            .thenAccept(result -> p.sendMessage(ChatHelper.color(" &8>> &7Zostales przeteleportowany do &anajblizszego gracza&7.")));
        }
    }
}
