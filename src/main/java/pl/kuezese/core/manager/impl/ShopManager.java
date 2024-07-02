package pl.kuezese.core.manager.impl;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.manager.Manager;
import pl.kuezese.core.object.ItemMaker;
import pl.kuezese.core.type.ShopType;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ShopManager extends Manager {

    private final List<ShopItem> items = new LinkedList<>();

    public ShopManager() {
        this.load();
    }

    private void load() {
        File file = new File(this.core.getDataFolder(), "shop.yml");

        if (!file.exists()) this.core.saveResource("shop.yml", true);

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.getConfigurationSection("shop").getKeys(false)
                .stream()
                .map(key -> new ShopItem(
                        ShopType.valueOf(cfg.getString("shop." + key + ".type")),
                        Material.matchMaterial(cfg.getString("shop." + key + ".item")),
                        (short) cfg.getInt("shop." + key + ".data"),
                        cfg.getInt("shop." + key + ".amount"),
                        cfg.getDouble("shop." + key + ".price"),
                        cfg.getString("shop." + key + ".name"),
                        cfg.getStringList("shop." + key + ".enchants")))
                .forEach(items::add);
    }

    public void reload() {
        this.items.clear();
        this.load();
    }

    public List<ShopItem> get(ShopType type) {
        return this.items.stream().filter(item -> item.getType() == type).collect(Collectors.toList());
    }

    public @Getter class ShopItem {

        private final ShopType type;
        private final Material item;
        private final short data;
        private final int amount;
        private final double price;
        private final String name;
        private final List<String> enchants;
        private final ItemStack itemStack;

        public ShopItem(ShopType type, Material item, short data, int amount, double price, String name, List<String> enchants) {
            this.type = type;
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