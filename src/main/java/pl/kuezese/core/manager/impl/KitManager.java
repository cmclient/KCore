package pl.kuezese.core.manager.impl;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.manager.Manager;
import pl.kuezese.core.object.ItemMaker;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class KitManager extends Manager {

    private final List<Kit> kits = new LinkedList<>();

    public KitManager() {
        this.load();
    }

    private void load() {
        File file = new File(this.core.getDataFolder(), "kits.yml");

        if (!file.exists()) this.core.saveResource("kits.yml", true);

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.getConfigurationSection("kits").getKeys(false).forEach(key -> {
            List<Item> items = cfg.getConfigurationSection("kits." + key + ".items").getKeys(false)
                    .stream()
                    .map(key1 -> new Item(
                            Material.matchMaterial(cfg.getString("kits." + key + ".items." + key1 + ".item")),
                            (short) cfg.getInt("kits." + key + ".items." + key1 + ".data"),
                            cfg.getInt("kits." + key + ".items." + key1 + ".amount"),
                            cfg.getString("kits." + key + ".items." + key1 + ".name"),
                            cfg.getStringList("kits." + key + ".items." + key1 + ".enchants")))
                    .collect(Collectors.toList());

            Kit kit = new Kit(
                    cfg.getString("kits." + key + ".name"),
                    Material.matchMaterial(cfg.getString("kits." + key + ".item")),
                    cfg.getString("kits." + key + ".permission"),
                    cfg.getLong("kits." + key + ".cooldown"),
                    items);

            this.kits.add(kit);
        });
    }

    public void reload() {
        this.kits.clear();
        this.load();
    }

    public @Getter class Kit {

        private final String name;
        private final String simpleName;
        private final Material item;
        private final String permission;
        private final long cooldown;
        private final List<Item> items;

        public Kit(String name, Material item, String permission, long cooldown, List<Item> items) {
            this.name = ChatHelper.color(name);
            this.simpleName = ChatColor.stripColor(this.name);
            this.item = item;
            this.permission = permission;
            this.cooldown = cooldown * 1000L;
            this.items = items;
        }
    }

    public @Getter class Item {
        private final Material item;
        private final short data;
        private final int amount;
        private final String name;
        private final List<String> enchants;
        private final ItemStack itemStack;

        public Item(Material item, short data, int amount, String name, List<String> enchants) {
            this.item = item;
            this.data = data;
            this.amount = amount;
            this.name = name;
            this.enchants = enchants;
            this.itemStack = new ItemMaker(item, amount, data).setName(ChatHelper.color(this.name)).make();
            enchants.stream().map(enchant -> enchant.split(":")).forEach(split -> this.itemStack.addUnsafeEnchantment(Enchantment.getByName(split[0]), Integer.parseInt(split[1])));
        }
    }
}