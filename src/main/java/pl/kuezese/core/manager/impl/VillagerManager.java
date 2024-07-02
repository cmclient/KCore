package pl.kuezese.core.manager.impl;

import lombok.Getter;
import lombok.Setter;
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
public class VillagerManager extends Manager {

    private final List<Villager> villagers = new LinkedList<>();

    public VillagerManager() {
        this.load();
    }

    private void load() {
        File file = new File(this.core.getDataFolder(), "villagers.yml");

        if (!file.exists()) this.core.saveResource("villagers.yml", true);

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.getConfigurationSection("villagers").getKeys(false).forEach(key -> {
            List<Item> items = cfg.getConfigurationSection("villagers." + key + ".items").getKeys(false)
                    .stream()
                    .map(key1 -> new Item(
                            Material.matchMaterial(cfg.getString("villagers." + key + ".items." + key1 + ".item")),
                            (short) cfg.getInt("villagers." + key + ".items." + key1 + ".data"),
                            cfg.getInt("villagers." + key + ".items." + key1 + ".amount"),
                            cfg.getInt("villagers." + key + ".items." + key1 + ".price"),
                            cfg.getString("villagers." + key + ".items." + key1 + ".name"),
                            cfg.getStringList("villagers." + key + ".items." + key1 + ".enchants")))
                    .collect(Collectors.toList());
            Villager villager = new Villager(
                    Material.matchMaterial(cfg.getString("villagers." + key + ".item")),
                    (short) cfg.getInt("villagers." + key + ".data"),
                    cfg.getString("villagers." + key + ".name"),
                    cfg.getStringList("villagers." + key + ".lore"),
                    items);
            villagers.add(villager);
        });
    }

    public void reload() {
        this.villagers.clear();
        this.load();
    }

    public Villager get(String name) {
        return villagers.stream().filter(villager -> villager.getName().equals(name)).findAny().orElse(null);
    }

    public @Getter @Setter class Villager {

        private Material item;
        private short data;
        private String name;
        private List<String> lore;
        private ItemStack itemStack;
        private List<Item> items;

        public Villager(Material item, short data, String name, List<String> lore, List<Item> items) {
            this.item = item;
            this.data = data;
            this.name = ChatHelper.color(name);
            this.lore = lore;
            this.itemStack = new ItemMaker(item, 1, data).setName(this.name).setLore(ChatHelper.color(lore)).make();
            this.items = items;
        }
    }

    public @Getter class Item {

        private final Material item;
        private final short data;
        private final int amount;
        private final int price;
        private final String name;
        private final List<String> enchants;
        private final ItemStack itemStack;

        public Item(Material item, short data, int amount, int price, String name, List<String> enchants) {
            this.item = item;
            this.data = data;
            this.amount = amount;
            this.price = price;
            this.name = ChatHelper.color(name);
            this.enchants = enchants;
            this.itemStack = new ItemMaker(item, amount, data).setName(this.name).make();
            enchants.stream().map(enchant -> enchant.split(":")).forEach(split -> this.itemStack.addUnsafeEnchantment(Enchantment.getByName(split[0]), Integer.parseInt(split[1])));
        }
    }
}
