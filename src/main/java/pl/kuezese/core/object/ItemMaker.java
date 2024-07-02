package pl.kuezese.core.object;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemMaker {

    private final Material mat;
    private final int amount;
    private final short data;
    private String name;
    private List<String> lore;
    private Map<Enchantment, Integer> enchants;
    private String owner;

    public ItemMaker(ItemStack stack) {
        this(stack.getType(), stack.getAmount(), stack.getDurability());
        if (stack.hasItemMeta()) {
            this.name = stack.getItemMeta().getDisplayName();
            if (stack.getItemMeta().getLore() != null) {
                this.lore = stack.getItemMeta().getLore();
            }
            if (stack.getEnchantments() != null && !stack.getEnchantments().isEmpty()) {
                (this.enchants = new HashMap<>()).putAll(stack.getEnchantments());
            }
        }
    }

    public ItemMaker(Material mat) {
        this(mat, 1);
    }

    public ItemMaker(Material mat, int amount) {
        this(mat, amount, (short) 0);
    }

    public ItemMaker(Material mat, int amount, short data) {
        this.mat = mat;
        this.amount = amount;
        this.data = data;
        this.name = null;
        this.owner = null;
    }

    public ItemMaker setName(String name) {
        this.name = name;
        return this;
    }

    public ItemMaker setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public ItemMaker addLore(String s) {
        if (this.lore == null)
            this.lore = new ArrayList<>();
        this.lore.add(s);
        return this;
    }

    public ItemMaker setEnchants(Map<Enchantment, Integer> enchants) {
        this.enchants = enchants;
        return this;
    }

    public ItemMaker addEnchant(Enchantment enchant, int level) {
        if (this.enchants == null)
            this.enchants = new HashMap<>();
        this.enchants.put(enchant, level);
        return this;
    }

    public ItemMaker setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemStack make() {
        ItemStack item = new ItemStack(this.mat, this.amount, this.data);
        ItemMeta meta = item.getItemMeta();
        if (this.name != null) {
            meta.setDisplayName(this.name);
        }
        if (this.lore != null && !this.lore.isEmpty()) {
            meta.setLore(this.lore);
        }
        if (this.owner != null && meta instanceof SkullMeta) {
            ((SkullMeta) meta).setOwner(this.owner);
        }
        item.setItemMeta(meta);
        if (this.enchants != null) {
            item.addUnsafeEnchantments(this.enchants);
        }
        return item;
    }
}
