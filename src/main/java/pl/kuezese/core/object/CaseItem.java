package pl.kuezese.core.object;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.kuezese.core.helper.ChatHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public @Getter class CaseItem {

    private final Material item;
    private final short data;
    private final int amount;
    private final double chance;
    private final String name;
    private final List<String> enchants;
    private final ItemStack itemStack;

    public CaseItem(Material item, short data, int amount, double chance, String name, List<String> enchants) {
        this.item = item;
        this.data = data;
        this.amount = amount;
        this.chance = chance;
        this.name = name;
        this.enchants = enchants;

        Map<Enchantment, Integer> enchantments = this.enchants.isEmpty() ? null : new HashMap<>();
        if (enchantments != null) {
            enchants.stream().map(s -> s.split(":")).forEach(strings -> enchantments.put(Enchantment.getByName(strings[0]), Integer.parseInt(strings[1])));
        }

        this.itemStack = new ItemMaker(item, amount, data).setEnchants(enchantments).setName(ChatHelper.color(this.name)).make();
    }
}