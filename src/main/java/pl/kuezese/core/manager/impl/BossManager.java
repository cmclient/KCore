package pl.kuezese.core.manager.impl;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.kuezese.core.boss.Boss;
import pl.kuezese.core.boss.stage.impl.Stage1;
import pl.kuezese.core.boss.stage.impl.Stage2;
import pl.kuezese.core.boss.stage.impl.Stage3;
import pl.kuezese.core.boss.stage.impl.Stage4;
import pl.kuezese.core.boss.stage.impl.szkieletor.Stage2Szkieletor;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.helper.RandomHelper;
import pl.kuezese.core.manager.Manager;
import pl.kuezese.core.object.ItemMaker;
import pl.kuezese.core.object.MobObject;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter @Setter
public class BossManager extends Manager {

    private Map<Integer, Boss> bosses = new LinkedHashMap<>();
    private Boss currentBoss;

    public BossManager() {
        this.load();
    }

    private void load() {
        AtomicInteger ai = new AtomicInteger();

        new Boss(this.core, "troll", "&9&lTROLL", 1000.0F, EntityType.ZOMBIE, true, Arrays.asList(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1))) {
            {
                this.setItemInHand(new ItemMaker(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 5).addEnchant(Enchantment.KNOCKBACK, 2).addEnchant(Enchantment.FIRE_ASPECT, 1).make());
                this.getArmor().add(new ItemMaker(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchant(Enchantment.DURABILITY, 2).make());
                this.getArmor().add(new ItemMaker(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchant(Enchantment.DURABILITY, 2).make());
                this.getArmor().add(new ItemMaker(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchant(Enchantment.DURABILITY, 2).make());
                this.getArmor().add(new ItemMaker(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).addEnchant(Enchantment.DURABILITY, 2).make());
                this.getDrops().put(new ItemMaker(Material.CHEST, 5).setName(ChatHelper.color("&8[&9&lPremiumCase&8]")).make(), 50.0D);
                this.getDrops().put(new ItemMaker(Material.CHEST, 10).setName(ChatHelper.color("&8[&9&lPremiumCase&8]")).make(), 60.0D);
                this.getDrops().put(new ItemMaker(Material.CHEST, 15).setName(ChatHelper.color("&8[&9&lPremiumCase&8]")).make(), 20.0D);
                this.getDrops().put(new ItemMaker(Material.CHEST, 25).setName(ChatHelper.color("&8[&9&lPremiumCase&8]")).make(), 10.0D);
                this.getDrops().put(new ItemMaker(Material.CHEST, 64).setName(ChatHelper.color("&8[&9&lPremiumCase&8]")).make(), 1.0D);
                this.getDrops().put(new ItemMaker(Material.DRAGON_EGG, 5).setName(ChatHelper.color("&8[&5&lPandora&8]")).make(), 85.0D);
                this.getDrops().put(new ItemMaker(Material.DRAGON_EGG, 7).setName(ChatHelper.color("&8[&5&lPandora&8]")).make(), 75.0D);
                this.getDrops().put(new ItemMaker(Material.DRAGON_EGG, 10).setName(ChatHelper.color("&8[&5&lPandora&8]")).make(), 60.0D);
                this.getDrops().put(new ItemMaker(Material.DRAGON_EGG, 15).setName(ChatHelper.color("&8[&5&lPandora&8]")).make(), 30.0D);
                this.getDrops().put(new ItemMaker(Material.DRAGON_EGG, 32).setName(ChatHelper.color("&8[&5&lPandora&8]")).make(), 5.0D);
                this.getDrops().put(new ItemMaker(Material.DRAGON_EGG, 64).setName(ChatHelper.color("&8[&5&lPandora&8]")).make(), 2.0D);
                this.getDrops().put(new ItemMaker(Material.DIAMOND_PICKAXE)
                        .addEnchant(Enchantment.DIG_SPEED, 10)
                        .addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3)
                        .addEnchant(Enchantment.DURABILITY, 3)
                        .setName(ChatHelper.color("&8>> &a&lNajszybszy Kilof &8(&a10/3/3&8)"))
                        .make(), 5.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e100$")).make(), 50.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e1000$")).make(), 5.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e10000$")).make(), 1.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e20000$")).make(), 0.25D);
                this.getDrops().put(new ItemMaker(Material.DIAMOND_PICKAXE)
                        .addEnchant(Enchantment.DIG_SPEED, 7)
                        .addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3)
                        .addEnchant(Enchantment.DURABILITY, 3)
                        .setName(ChatHelper.color("&8>> &a&lSzybki Kilof &8(&a7/3/3&8)"))
                        .make(), 45.0D);
                this.getDrops().put(new ItemMaker(Material.DIAMOND_PICKAXE)
                        .addEnchant(Enchantment.DIG_SPEED, 8)
                        .addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3)
                        .addEnchant(Enchantment.DURABILITY, 3)
                        .setName(ChatHelper.color("&8>> &a&lMega Szybki Kilof &8(&a8/3/3&8)"))
                        .make(), 30.0D);
                this.getDrops().put(new ItemMaker(Material.BOW)
                        .addEnchant(Enchantment.ARROW_DAMAGE, 6)
                        .addEnchant(Enchantment.ARROW_INFINITE, 1)
                        .addEnchant(Enchantment.ARROW_FIRE, 2)
                        .addEnchant(Enchantment.DURABILITY, 3)
                        .setName(ChatHelper.color("&8>> &a&lLuk RobinHood'a"))
                        .make(), 15.0D);
                this.getDrops().put(new ItemMaker(Material.EMERALD_BLOCK, 64).setName(ChatHelper.color("&8>> &aBloki emeraldow")).make(), 80.0D);
                this.getDrops().put(new ItemMaker(Material.DIAMOND_BLOCK, 64).setName(ChatHelper.color("&8>> &bBloki diamentow")).make(), 80.0D);
                this.getDrops().put(new ItemMaker(Material.TNT, 5)
                        .addEnchant(Enchantment.DURABILITY, 1)
                        .setName(ChatHelper.color("&8>> &7Rzucane TNT"))
                        .make(), 5.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e100$")).make(), 50.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e1000$")).make(), 15.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e2000$")).make(), 10.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e5000$")).make(), 5.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e10000$")).make(), 1.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e20000$")).make(), 0.1D);
//                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.DURABILITY, 1).setName(ChatHelper.color("&7Voucher na &aSVipa")).make(), 50.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.DURABILITY, 1).setName(ChatHelper.color("&7Voucher na &3Sponsora")).make(), 25.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.DURABILITY, 1).setName(ChatHelper.color("&7Voucher na &1&lL&2&lE&3&lG&4&lE&5&lN&6&lD&7&lA")).make(), 5.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.DURABILITY, 1).setName(ChatHelper.color("&7Voucher na &a&lE&b&lL&c&lI&d&lT&e&lA")).make(), 0.25D);
                this.getDrops().put(new ItemMaker(Material.BEACON).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&c&lB&e&le&a&la&b&lc&3&lo&d&ln")).make(), 7.0D);
                this.getDrops().put(new ItemMaker(Material.STRING, 64).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Pajeczynki")).make(), 50.0D);
                this.getStages().add(new Stage1(this, 90.0D, 20L, 1.5D,
                        new MobObject(EntityType.ZOMBIE, "Kompani Trolla", false),
                        new MobObject(EntityType.SKELETON, "Kompani Trolla", false)));
                this.getStages().add(new Stage2(this, 60.0D, 10L, 2.0D,
                        new MobObject(EntityType.CREEPER, "Kompani Trolla", false),
                        new MobObject(EntityType.WITCH, "Kompani Trolla", false)));
                this.getStages().add(new Stage3(this, 30.0D, 2L, 2.5D,
                        new MobObject(EntityType.CREEPER, "Kompani Trolla", true),
                        new MobObject(EntityType.PIG_ZOMBIE, "Kompani Trolla", false)));
                this.getStages().add(new Stage4(this, 10.0D, 5L, 3.0D,
                        new MobObject(EntityType.CREEPER, "Kompani Trolla", true),
                        new MobObject(EntityType.PIG_ZOMBIE, "Kompani Trolla", false)));
                BossManager.this.bosses.put(ai.getAndIncrement(), this);
            }
        };

        new Boss(this.core, "szkieletor", "&e&lSZKIELETOR", 750.0F, EntityType.SKELETON, true, Arrays.asList(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4), new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 3))) {
            {
                this.setItemInHand(new ItemMaker(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 6).addEnchant(Enchantment.ARROW_KNOCKBACK, 2).addEnchant(Enchantment.ARROW_FIRE, 1).addEnchant(Enchantment.ARROW_INFINITE, 1).make());
                this.getArmor().add(new ItemMaker(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).make());
                this.getArmor().add(new ItemMaker(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).make());
                this.getArmor().add(new ItemMaker(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).make());
                this.getArmor().add(new ItemMaker(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchant(Enchantment.DURABILITY, 3).make());
                this.getDrops().put(new ItemMaker(Material.CHEST, 5).setName(ChatHelper.color("&8[&9&lPremiumCase&8]")).make(), 50.0D);
                this.getDrops().put(new ItemMaker(Material.CHEST, 10).setName(ChatHelper.color("&8[&9&lPremiumCase&8]")).make(), 60.0D);
                this.getDrops().put(new ItemMaker(Material.CHEST, 15).setName(ChatHelper.color("&8[&9&lPremiumCase&8]")).make(), 20.0D);
                this.getDrops().put(new ItemMaker(Material.CHEST, 25).setName(ChatHelper.color("&8[&9&lPremiumCase&8]")).make(), 10.0D);
                this.getDrops().put(new ItemMaker(Material.CHEST, 64).setName(ChatHelper.color("&8[&9&lPremiumCase&8]")).make(), 1.0D);
                this.getDrops().put(new ItemMaker(Material.DRAGON_EGG, 5).setName(ChatHelper.color("&8[&5&lPandora&8]")).make(), 75.0D);
                this.getDrops().put(new ItemMaker(Material.DRAGON_EGG, 10).setName(ChatHelper.color("&8[&5&lPandora&8]")).make(), 60.0D);
                this.getDrops().put(new ItemMaker(Material.DRAGON_EGG, 15).setName(ChatHelper.color("&8[&5&lPandora&8]")).make(), 30.0D);
                this.getDrops().put(new ItemMaker(Material.DRAGON_EGG, 32).setName(ChatHelper.color("&8[&5&lPandora&8]")).make(), 5.0D);
                this.getDrops().put(new ItemMaker(Material.DRAGON_EGG, 64).setName(ChatHelper.color("&8[&5&lPandora&8]")).make(), 2.0D);
                this.getDrops().put(new ItemMaker(Material.DIAMOND_PICKAXE)
                        .addEnchant(Enchantment.DIG_SPEED, 7)
                        .addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3)
                        .addEnchant(Enchantment.DURABILITY, 3)
                        .setName(ChatHelper.color("&8>> &a&lSzybki Kilof &8(&a7/3/3&8)"))
                        .make(), 40.0D);
                this.getDrops().put(new ItemMaker(Material.DIAMOND_PICKAXE)
                        .addEnchant(Enchantment.DIG_SPEED, 8)
                        .addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3)
                        .addEnchant(Enchantment.DURABILITY, 3)
                        .setName(ChatHelper.color("&8>> &a&lMega Szybki Kilof &8(&a8/3/3&8)"))
                        .make(), 25.0D);
                this.getDrops().put(new ItemMaker(Material.DIAMOND_PICKAXE)
                        .addEnchant(Enchantment.DIG_SPEED, 10)
                        .addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 3)
                        .addEnchant(Enchantment.DURABILITY, 3)
                        .setName(ChatHelper.color("&8>> &a&lNajszybszy Kilof &8(&a10/3/3&8)"))
                        .make(), 5.0D);
                this.getDrops().put(new ItemMaker(Material.EMERALD_BLOCK, 64).setName(ChatHelper.color("&8>> &aBloki emeraldow")).make(), 80.0D);
                this.getDrops().put(new ItemMaker(Material.DIAMOND_BLOCK, 64).setName(ChatHelper.color("&8>> &bBloki diamentow")).make(), 80.0D);
                this.getDrops().put(new ItemMaker(Material.TNT, 5)
                        .addEnchant(Enchantment.DURABILITY, 1)
                        .setName(ChatHelper.color("&8>> &7Rzucane TNT"))
                        .make(), 5.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e100$")).make(), 50.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e1000$")).make(), 15.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e2000$")).make(), 10.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e5000$")).make(), 5.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e10000$")).make(), 1.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Voucher na &e20000$")).make(), 0.1D);
//                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.DURABILITY, 1).setName(ChatHelper.color("&7Voucher na &aSVipa")).make(), 50.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.DURABILITY, 1).setName(ChatHelper.color("&7Voucher na &3Sponsora")).make(), 25.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.DURABILITY, 1).setName(ChatHelper.color("&7Voucher na &1&lL&2&lE&3&lG&4&lE&5&lN&6&lD&7&lA")).make(), 5.0D);
                this.getDrops().put(new ItemMaker(Material.PAPER).addEnchant(Enchantment.DURABILITY, 1).setName(ChatHelper.color("&7Voucher na &a&lE&b&lL&c&lI&d&lT&e&lA")).make(), 0.25D);
                this.getDrops().put(new ItemMaker(Material.MILK_BUCKET).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&8>> &aAnty-Nogi")).make(), 10.0D);
                this.getDrops().put(new ItemMaker(Material.BEACON).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&c&lB&e&le&a&la&b&lc&3&lo&d&ln")).make(), 5.0D);
                this.getDrops().put(new ItemMaker(Material.BOW)
                        .addEnchant(Enchantment.ARROW_DAMAGE, 6)
                        .addEnchant(Enchantment.ARROW_INFINITE, 1)
                        .addEnchant(Enchantment.ARROW_FIRE, 2)
                        .addEnchant(Enchantment.DURABILITY, 3)
                        .setName(ChatHelper.color("&8>> &a&lLuk RobinHood'a"))
                        .make(), 15.0D);
                this.getDrops().put(new ItemMaker(Material.BOW)
                        .addEnchant(Enchantment.ARROW_KNOCKBACK, 3)
                        .addEnchant(Enchantment.ARROW_INFINITE, 1)
                        .addEnchant(Enchantment.DURABILITY, 3)
                        .setName(ChatHelper.color("&8>> &a&lPunch &8(&a3&8)"))
                        .make(), 35.0D);
                this.getDrops().put(new ItemMaker(Material.FISHING_ROD)
                        .addEnchant(Enchantment.DURABILITY, 10)
                        .setName(ChatHelper.color("&8>> &a&lWedka Karpiowa"))
                        .make(), 45.0D);
                this.getDrops().put(new ItemMaker(Material.STRING, 64).addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 1).setName(ChatHelper.color("&7Pajeczynki")).make(), 50.0D);
                this.getStages().add(new Stage1(this, 90.0D, 20L, 2,
                        new MobObject(EntityType.ZOMBIE, "Kompani Szkieletora", false),
                        new MobObject(EntityType.SKELETON, "Kompani Szkieletora", false)));
                this.getStages().add(new Stage2Szkieletor(this, 60.0D, 4L, 3,
                        new MobObject(EntityType.SKELETON, "Kompani Szkieletora", true),
                        new MobObject(EntityType.CREEPER, "Kompani Szkieletora", true)));
                this.getStages().add(new Stage3(this, 30.0D, 2L, 4,
                        new MobObject(EntityType.SKELETON, "Kompani Szkieletora", true),
                        new MobObject(EntityType.CREEPER, "Kompani Szkieletora", true),
                        new MobObject(EntityType.PIG_ZOMBIE, "Kompani Szkieletora", false)));
                BossManager.this.bosses.put(ai.getAndIncrement(), this);
            }
        };
    }


    public List<String> getTypes() {
        return this.bosses.values().stream().map(Boss::getType).collect(Collectors.toList());
    }

    public Boss get(String type) {
        return this.bosses.values().stream().filter(boss -> boss.getType().equalsIgnoreCase(type)).findAny().orElse(null);
    }

    public Boss get(int id) {
        return this.bosses.get(id);
    }

    public void spawn() {
        this.spawn(this.bosses.get(RandomHelper.nextInt(this.bosses.size())));
    }

    public void spawn(Boss boss) {
        if (this.currentBoss != null) {
            this.currentBoss.kill();
        }

        this.currentBoss = boss;
        Location location = this.currentBoss.spawn(this.core.getServer().getWorlds().get(0));

        this.core.getServer().broadcastMessage(ChatHelper.color(" &8&m------------------------------"));
        this.core.getServer().broadcastMessage("");
        this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Na serwerze pojawil sie Boss &9" + StringUtils.capitalize(ChatColor.stripColor(boss.getName().toLowerCase()))));
        this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Sprawdz nagrody za zabicie bossa pod /drop"));
        this.core.getServer().broadcastMessage(ChatHelper.color(" &8>> &7Koordynaty: X: &9" + location.getBlockX() + " &7Y: &9" + location.getBlockY() + " &7Z: &9" + location.getBlockZ()));
        this.core.getServer().broadcastMessage("");
        this.core.getServer().broadcastMessage(ChatHelper.color(" &8&m------------------------------"));

        this.core.getServer().getOnlinePlayers().forEach(player -> {
            ChatHelper.title(player, "&8• &2&lBOSS &8•", "&7Pojawil sie Boss: &9" + StringUtils.capitalize(ChatColor.stripColor(boss.getName().toLowerCase())), 10, 30, 10);
            player.playEffect(EntityEffect.FIREWORK_EXPLODE);
            player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
        });
    }

    public void kill() {
        this.currentBoss.kill();
        this.currentBoss = null;
    }
}