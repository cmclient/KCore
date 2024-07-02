package pl.kuezese.core.listener.impl;

import org.bukkit.DyeColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import pl.kuezese.core.listener.Listener;

public class EnchantItemListener extends Listener {

    private final ItemStack lapis;

    public EnchantItemListener() {
        Dye d = new Dye();
        d.setColor(DyeColor.BLUE);
        (this.lapis = d.toItemStack()).setAmount(3);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEnchant(EnchantItemEvent e) {
        if (e.getInventory() instanceof EnchantingInventory) {
            e.getInventory().setItem(1, this.lapis);
        }
    }
}
