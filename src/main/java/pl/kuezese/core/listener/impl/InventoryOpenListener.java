package pl.kuezese.core.listener.impl;

import org.bukkit.DyeColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import pl.kuezese.core.helper.ChatHelper;
import pl.kuezese.core.object.User;
import pl.kuezese.core.listener.Listener;

public class InventoryOpenListener extends Listener {

    private final ItemStack lapis;

    public InventoryOpenListener() {
        Dye d = new Dye();
        d.setColor(DyeColor.BLUE);
        (this.lapis = d.toItemStack()).setAmount(3);
    }

    @EventHandler(ignoreCancelled = true)
    public void onOpen(InventoryOpenEvent e) {
        if (e.getInventory() instanceof EnchantingInventory) {
            e.getInventory().setItem(1, this.lapis);
            return;
        }

        HumanEntity p = e.getPlayer();

        if (e.getInventory().getType() != InventoryType.PLAYER
                && !e.getInventory().getName().equals(ChatHelper.color("&8>> &aMenu efektow &8<<"))
                && !e.getInventory().getName().equals(ChatHelper.color("&8>> &aEfekty dla &agracza &8<<"))
                && !e.getInventory().getName().equals(ChatHelper.color("&8>> &aEfekty dla &agildii &8<<"))
                && !p.hasPermission("cm.bypass")) {
            User user = this.userManager.get(p.getName());
            if (user.isCombat()) {
                e.setCancelled(true);
                p.sendMessage(ChatHelper.color(" &8>> &7Nie mozesz tego otworzyc podczas &cwalki&7."));
            }
        }
    }
}
