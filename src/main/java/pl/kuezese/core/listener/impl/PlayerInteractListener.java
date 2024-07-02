package pl.kuezese.core.listener.impl;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import pl.kuezese.core.menu.SafeMenu;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.RandomHelper;
import pl.kuezese.core.listener.Listener;
import pl.kuezese.core.object.User;
import pl.kuezese.region.object.Region;
import pl.kuezese.region.type.Flag;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

public class PlayerInteractListener extends Listener {

    private final Map<String, Long> times = new WeakHashMap<>();

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block cblock = e.getClickedBlock();

            if (cblock.getType() == Material.ENDER_CHEST) {
                e.setCancelled(true);
                new SafeMenu(this.core).open(p);
                return;
            }

            if ((cblock.getType() == Material.STONE_BUTTON) || (cblock.getType() == Material.WOOD_BUTTON)) {
                Location block = cblock.getLocation().add(1.0D, 0.0D, 0.0D);
                Location block1 = cblock.getLocation().add(-1.0D, 0.0D, 0.0D);
                Location block2 = cblock.getLocation().add(0.0D, 0.0D, 1.0D);
                Location block3 = cblock.getLocation().add(0.0D, 0.0D, -1.0D);

                if ((block.getBlock().getType() == Material.JUKEBOX) || (block1.getBlock().getType() == Material.JUKEBOX) || (block2.getBlock().getType() == Material.JUKEBOX) || (block3.getBlock().getType() == Material.JUKEBOX)) {
                    RandomHelper.teleport(p, RandomHelper.randomLoc(p.getWorld(), 4000));
                    return;
                }

                if ((block.getBlock().getType() == Material.NOTE_BLOCK) || (block1.getBlock().getType() == Material.NOTE_BLOCK) || (block2.getBlock().getType() == Material.NOTE_BLOCK) || (block3.getBlock().getType() == Material.NOTE_BLOCK)) {
                    Location clickerLocation = p.getLocation();

                    if (clickerLocation.getBlock().getType() != Material.STONE_PLATE) {
                        p.sendMessage(ChatHelper.color(" &8>> &cMusisz stac na polplytce."));
                        return;
                    }

                    Location location = RandomHelper.randomLoc(p.getWorld(), 4000);

                    List<Player> players = p.getWorld().getPlayers()
                            .stream()
                            .filter(other -> other != p && clickerLocation.distance(other.getLocation()) <= 10.0 && other.getLocation().getBlock().getType() == Material.STONE_PLATE)
                            .collect(Collectors.toList());

                    if (players.isEmpty()) {
                        p.sendMessage(ChatHelper.color(" &8>> &cNie masz z kim sie przeteleportowac."));
                        return;
                    }

                    p.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(p::removePotionEffect);
                    RandomHelper.teleport(p, location);
                    User user = this.userManager.get(p.getName());
                    user.setTeleportTime(System.currentTimeMillis() + 5000L);

                    players.forEach(other -> {
                        other.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(other::removePotionEffect);
                        RandomHelper.teleport(other, location);
                        User user1 = this.userManager.get(other.getName());
                        user1.setTeleportTime(System.currentTimeMillis() + 5000L);
                    });
                    return;
                }

                if ((block.getBlock().getType() == Material.BEDROCK) || (block1.getBlock().getType() == Material.BEDROCK) || (block2.getBlock().getType() == Material.BEDROCK) || (block3.getBlock().getType() == Material.BEDROCK)) {
                    Location clickerLocation = p.getLocation();

                    if (clickerLocation.getBlock().getType() != Material.STONE_PLATE) {
                        p.sendMessage(ChatHelper.color(" &8>> &cMusisz stac na polplytce."));
                        return;
                    }

                    List<Player> players = p.getWorld().getPlayers()
                            .stream()
                            .filter(other -> other != p && clickerLocation.distance(other.getLocation()) <= 10.0 && other.getLocation().getBlock().getType() == Material.STONE_PLATE).collect(Collectors.toList());

                    Player target = players.stream().skip((int) (players.size() * Math.random())).findAny().orElse(null);

                    if (target == null) {
                        p.sendMessage(ChatHelper.color(" &8>> &cNie masz z kim sie przeteleportowac."));
                        return;
                    }

                    Location location = RandomHelper.randomLoc(p.getWorld(), 4000);

                    p.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(p::removePotionEffect);
                    RandomHelper.teleport(p, location);
                    User user = this.userManager.get(p.getName());
                    user.setTeleportTime(System.currentTimeMillis() + 5000L);

                    target.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(target::removePotionEffect);
                    RandomHelper.teleport(target, location);
                    User user1 = this.userManager.get(target.getName());
                    user1.setTeleportTime(System.currentTimeMillis() + 5000L);
                }
                return;
            }
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (p.getItemInHand().getType() == Material.TNT && p.getItemInHand().getItemMeta().getDisplayName() != null && p.getItemInHand().getItemMeta().getDisplayName().equals(ChatHelper.color("&8>> &7Rzucane TNT"))) {
                e.setCancelled(true);

                Region region = this.regionManager.find(p.getLocation());

                if (region != null && region.getFlags().contains(Flag.explode)) {
                    return;
                }

                if (times.containsKey(p.getName()) && times.get(p.getName()) > System.currentTimeMillis()) {
                    long time = times.get(p.getName());
                    p.sendMessage(ChatHelper.color(" &8>> &7Kolejny raz bedziesz mogl uzyc Rzucane TNT za: &a" + (time - System.currentTimeMillis()) / 1000 + "s&7."));
                    return;
                } else times.put(p.getName(), System.currentTimeMillis() + 2500L);

                if (p.getItemInHand().getAmount() > 1) p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
                else p.setItemInHand(null);

                TNTPrimed tnt = p.getWorld().spawn(p.getEyeLocation(), TNTPrimed.class);
                tnt.setVelocity(p.getEyeLocation().getDirection().normalize().multiply(1));
                return;
            }

            if (p.getItemInHand().getType() == Material.PAPER && p.getItemInHand().containsEnchantment(Enchantment.DURABILITY) && p.getItemInHand().getItemMeta().getDisplayName() != null) {
                String group;
                String displayName = p.getItemInHand().getItemMeta().getDisplayName();

                if (displayName.equals(ChatHelper.color("&7Voucher na range: &9&lSPONSOR"))) {
                    group = "Sponsor";
                } else if (displayName.equals(ChatHelper.color("&7Voucher na range: &1&lL&2&lE&3&lG&4&lE&5&lN&6&lD&7&lA"))) {
                    group = "Legenda";
                } else if (displayName.equals(ChatHelper.color("&7Voucher na range: &a&lE&b&lL&c&lI&d&lT&e&lA"))) {
                    group = "Elita";
                } else {
                    group = null;
                }

                if (group != null) {
                    e.setCancelled(true);

                    if (p.getItemInHand().getAmount() > 1)
                        p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
                    else p.setItemInHand(null);

                    this.core.getServer().dispatchCommand(this.core.getServer().getConsoleSender(), "lp user " + p.getName() + " group set " + group);
                    this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                    this.core.getServer().broadcastMessage("");
                    this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &f&n" + p.getName() + "&r &7uzyl voucher na range &f&l" + group));
                    this.core.getServer().broadcastMessage("");
                    this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                    this.core.getServer().getOnlinePlayers().forEach(o -> ChatHelper.title(o, "&8• &2&lVOUCHER &8•", "&7Gracz &f&n" + p.getName() + "&r &7uzyl voucher na range &f&l" + group, 10, 30, 10));
                }
                return;
            }

            if (p.getItemInHand().getType() == Material.PAPER && p.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS) && p.getItemInHand().getItemMeta().getDisplayName() != null) {
                float coins;

                if (p.getItemInHand().getItemMeta().getDisplayName().equals(ChatHelper.color("&7Voucher na &e100$"))) {
                    coins = 100.0F;
                } else if (p.getItemInHand().getItemMeta().getDisplayName().equals(ChatHelper.color("&7Voucher na &e1000$"))) {
                    coins = 1000.0F;
                } else if (p.getItemInHand().getItemMeta().getDisplayName().equals(ChatHelper.color("&7Voucher na &e2000$"))) {
                    coins = 2000.0F;
                } else if (p.getItemInHand().getItemMeta().getDisplayName().equals(ChatHelper.color("&7Voucher na &e5000$"))) {
                    coins = 5000.0F;
                } else if (p.getItemInHand().getItemMeta().getDisplayName().equals(ChatHelper.color("&7Voucher na &e10000$"))) {
                    coins = 10000.0F;
                } else if (p.getItemInHand().getItemMeta().getDisplayName().equals(ChatHelper.color("&7Voucher na &e20000$"))) {
                    coins = 20000.0F;
                } else {
                    coins = Float.MIN_VALUE;
                }

                if (coins != Float.MIN_VALUE) {
                    e.setCancelled(true);

                    if (p.getItemInHand().getAmount() > 1)
                        p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
                    else p.setItemInHand(null);

                    User user = this.userManager.get(p.getName());
                    user.addCoins(coins);
                    this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                    this.core.getServer().broadcastMessage("");
                    this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Gracz &f&n" + p.getName() + "&r &7uzyl voucher na &e&l" + coins + "$"));
                    this.core.getServer().broadcastMessage("");
                    this.core.getServer().broadcastMessage(ChatHelper.color("&8&m----------------------------------"));
                    this.core.getServer().getOnlinePlayers().forEach(o -> ChatHelper.title(o, "&8• &2&lVOUCHER &8•", "&7Gracz &f&n" + p.getName() + "&r &7uzyl voucher na &e&l" + coins + "$", 10, 30, 10));
                }
            }
        }
    }
}
